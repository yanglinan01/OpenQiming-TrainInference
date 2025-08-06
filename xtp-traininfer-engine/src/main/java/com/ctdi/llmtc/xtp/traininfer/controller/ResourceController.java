package com.ctdi.llmtc.xtp.traininfer.controller;

import cn.hutool.core.util.ObjectUtil;
import com.ctdi.llmtc.xtp.traininfer.beans.req.TaskReq;
import com.ctdi.llmtc.xtp.traininfer.beans.resp.NodeResp;
import com.ctdi.llmtc.xtp.traininfer.beans.resp.TaskResp;
import com.ctdi.llmtc.xtp.traininfer.service.ResourceService;
import com.ctdi.llmtc.xtp.traininfer.util.JSONUtil;
import com.ctdi.llmtc.xtp.traininfer.util.OperateResult2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 资源对外接口控制器类。
 *
 */
@RestController
//@RequestMapping("/resource")
@Slf4j
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/nodes/info")
    public OperateResult2<List<NodeResp>> queryNodesInfo() {
        log.info("ResourceController nodes info request");
        List<NodeResp> data = resourceService.getNodeInfo();
        OperateResult2<List<NodeResp>> result;
        if (ObjectUtil.isNotEmpty(data)) {
            result = OperateResult2.success(data);
        } else {
            result = OperateResult2.fail(data);
        }
        log.info("ResourceController nodes info success");
        return result;
    }

    @PostMapping("/task/info")
    public OperateResult2<List<TaskResp>> queryTaskInfo(@RequestBody TaskReq taskReq) {
        log.info("[{}] ResourceController queryTaskInfo request [{}]", taskReq.getTaskId(), JSONUtil.toJSONString(taskReq));
        List<TaskResp> data = resourceService.getTaskInfo(taskReq);
        OperateResult2<List<TaskResp>> result;
        if (ObjectUtil.isNotEmpty(data)) {
            result = OperateResult2.success(data);
        } else {
            result = OperateResult2.fail(data);
        }
        log.info("[{}] ResourceController queryTaskInfo response [{}]", taskReq.getTaskId(), JSONUtil.toJSONString(result));
        return result;
    }

}
