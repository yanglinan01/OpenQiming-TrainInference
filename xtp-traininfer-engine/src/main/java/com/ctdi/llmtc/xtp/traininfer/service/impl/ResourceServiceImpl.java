package com.ctdi.llmtc.xtp.traininfer.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ctdi.llmtc.xtp.traininfer.beans.req.TaskReq;
import com.ctdi.llmtc.xtp.traininfer.beans.resp.NodeResp;
import com.ctdi.llmtc.xtp.traininfer.beans.resp.TaskResp;
import com.ctdi.llmtc.xtp.traininfer.service.ResourceService;
import com.ctdi.llmtc.xtp.traininfer.util.K8sUtil;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeAddress;
import io.fabric8.kubernetes.api.model.Pod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ctdi
 * @since 2025/6/5
 */
@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    /** k8s工具实例 */
    public static final K8sUtil k8sUtil = K8sUtil.getInstance();

    private static final Pattern GPU_PATTERN = Pattern.compile("NVIDIA_VISIBLE_DEVICES=(.+?)\n|ASCEND_VISIBLE_DEVICES=(.+?)\n");

    @Override
    public List<NodeResp> getNodeInfo() {
        List<NodeResp> data = new ArrayList<>();
        List<Node> nodeList = k8sUtil.getNodeList();
        for (Node node : nodeList) {
            // 检查节点是否可调度
            boolean isSchedulingDisabled = node.getStatus().getConditions().stream()
                    .anyMatch(condition -> "SchedulingDisabled".equals(condition.getType()));

            if (isSchedulingDisabled) {
                continue;
            }
            // 获取节点IP
            String ip = node.getStatus().getAddresses().stream()
                    .findFirst()
                    .map(NodeAddress::getAddress)
                    .orElse("");

            // 排除master节点
            if (ObjectUtil.isEmpty(node.getMetadata().getLabels())
                    || node.getMetadata().getName().contains("master")) {
                continue;
            }

            Map<String, Boolean> labelsDict = new HashMap<>();
            Map<String, String> labels = node.getMetadata().getLabels();

            Optional.ofNullable(labels.get("train"))
                    .ifPresent(v -> labelsDict.put("train", "true".equals(v)));
            Optional.ofNullable(labels.get("inference"))
                    .ifPresent(v -> labelsDict.put("inference", "true".equals(v)));
            // 添加节点信息
            NodeResp resp = new NodeResp();
            resp.setNodeName(node.getMetadata().getName());
            resp.setNodeIp(ip);
            resp.setNodeLabels(labelsDict);
            data.add(resp);
        }
        return data;
    }

    @Override
    public List<TaskResp> getTaskInfo(TaskReq taskReq) {
        List<TaskResp> data = new ArrayList<>();

        String taskType = taskReq.getTaskType();
        String taskId = taskReq.getTaskId();
        String namespace = taskType.equals("train") ? "qwen2-train" : "qwen2-inference";

        List<Pod> podList = k8sUtil.getPodList(namespace);
        podList.stream().filter(pod -> pod.getMetadata().getName().contains(taskId)).forEach(p -> {
            String podName = p.getMetadata().getName();
            String containerName = p.getMetadata().getAnnotations()
                    .get("kubectl.kubernetes.io/default-container");
            String[] command = {"/bin/sh", "-c", "env"};
            String output = k8sUtil.execCmdInPod(namespace, podName, containerName, command);

            //output = """
            //            INFERENCE_SERVICE_1854818810332794880_PORT_8000_TCP_ADDR=192.141.91.221
            //            INFERENCE_SERVICE_1862066671378710528_PORT_8000_TCP_PORT=8000
            //            INFERENCE_SERVICE_1878693311079395328_SERVICE_HOST=192.132.164.149
            //            INFERENCE_SERVICE_1869674831576522752_PORT=tcp://192.134.19.71:8000
            //            ASCEND_VISIBLE_DEVICES=5,6
            //            INFERENCE_SERVICE_1862066671378710528_SERVICE_PORT=8000
            //            INFERENCE_SERVICE_1878629340280401920_PORT_8000_TCP_PROTO=tcp
            //            INFERENCE_SERVICE_1877552569998073856_PORT_8000_TCP_PORT=8000
            //        """;

            Matcher matcher = GPU_PATTERN.matcher(output);
            if (matcher.find()) {
                String gpuIdsStr = matcher.group().trim()
                        .replace("ASCEND_VISIBLE_DEVICES=", "")
                        .replace("NVIDIA_VISIBLE_DEVICES=", "");
                String[] gpuIds = gpuIdsStr.split(",");
                TaskResp resp = new TaskResp();
                resp.setNodeName(p.getSpec().getNodeName());
                resp.setGpuIds(gpuIds);
                data.add(resp);
            }
        });
        return data;
    }
}
