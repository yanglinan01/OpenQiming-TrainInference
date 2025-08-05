package com.ctdi.cnos.llm.metadata.extra.cluster.processor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetric;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetricVO;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterVO;
import com.ctdi.cnos.llm.beans.meta.metric.*;
import com.ctdi.cnos.llm.metadata.client.MetricApiClient;
import com.ctdi.cnos.llm.metadata.extra.cluster.BaseClusterResourceStatItemProcessor;
import com.ctdi.cnos.llm.metadata.service.ClusterMetricService;
import com.ctdi.cnos.llm.metadata.service.ClusterService;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.ModelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuyong
 * @date 2024/9/25 15:06
 */
@Component
@Slf4j
public class ResourceUsageDetailProcessor extends BaseClusterResourceStatItemProcessor {

    private ClusterService clusterService;
    private ClusterMetricService clusterMetricService;
    private MetricApiClient metricApiClient;

    public ResourceUsageDetailProcessor(ClusterService clusterService, ClusterMetricService clusterMetricService, MetricApiClient metricApiClient) {
        super(ClusterResourceStatItem.RESOURCE_USAGE_DETAIL);
        this.clusterService = clusterService;
        this.clusterMetricService = clusterMetricService;
        this.metricApiClient = metricApiClient;
    }

    @Override
    protected OperateResult<?> execute(ClusterStatParam params) {
        log.info("进来ResourceUsageDetailProcessor了");
        ResourceUsageDetail detail = new ResourceUsageDetail();
        List<ClusterNodeCard> nodeCardList = new ArrayList<>();
        List<ClusterVO> clusterVoList = new ArrayList<>();

        // 查询对应绑定的指标
        QueryParam queryParam = new QueryParam();
        ClusterMetric clusterMetricQuery = new ClusterMetric();
        clusterMetricQuery.setClusterCode(params.getClusterCode());
        clusterMetricQuery.setCategory(ClusterResourceStatItem.RESOURCE_USAGE_DETAIL.name());
        queryParam.setFilterDto(clusterMetricQuery);
        List<ClusterMetricVO> clusterMetricVoList = clusterMetricService.queryList(queryParam);

        ClusterVO cluster = new ClusterVO() ;
        if (params!= null && CharSequenceUtil.isNotBlank(params.getClusterCode())) {
            cluster = clusterService.queryByCode(params.getClusterCode());
            if (cluster!= null) {
                clusterVoList.add(cluster);
            }
        } else {
            clusterVoList = clusterService.queryList(new QueryParam());
        }

        if (CollUtil.isNotEmpty(clusterVoList)) {
            Map<String, List<String>> nodeCardMap = new HashMap<>();
            for (ClusterVO clusters : clusterVoList) {
                detail.setClusterName(clusters.getName());
                JSONObject object = new JSONObject();
                object.put("task_id", params.getTaskId().toString());
                object.put("task_type", params.getTaskType());
                log.info("进来queryNodeCardTrainAndInference了入参为:" + object.toString());
                log.info("进来queryNodeCardTrainAndInference了接口地址为:" + clusters.getNodeHostUrl());
                nodeCardList = metricApiClient.queryNodeCardTrainAndInference(clusters.getNodeHostUrl(), object);
                log.info("queryNodeCardTrainAndInference结果为:" + nodeCardList.size());
                int cardNum = 0;
                if (CollUtil.isNotEmpty(nodeCardList)) {
                    for (ClusterNodeCard card : nodeCardList) {
                        if(null != card){
                            nodeCardMap.put(card.getNodeName(), card.getGpuIds());
                            cardNum += card.getGpuIds().size();
                        }
                    }

                    log.info("nodeCardMap为:" + nodeCardMap);
                    if(CollUtil.isNotEmpty(nodeCardMap)){
                        if (CollUtil.isNotEmpty(clusterMetricVoList)) {
                            for (ClusterMetricVO clusterMetric : clusterMetricVoList) {
                                String paramStr = buildParamStr(params.getClusterCode(), clusterMetric, nodeCardMap);
                                log.info("查询参数为:" + paramStr);
                                List<MetricValue<String>> metricValueList = metricApiClient.querySingleMetricValue(paramStr, clusters.getPrometheusHostUrl(), String.class);
                                if (CollUtil.isNotEmpty(metricValueList)) {
                                    handleMetricValue(clusterMetric.getEntryField(), metricValueList, detail, clusters.getCode());
                                }
                            }
                        }
                    }
                }else{
                    log.error("无法根据入参:" + object.toJSONString() + "在接口:" + clusters.getNodeHostUrl() + "获取到训练推理任务所占节点及所用卡数");
                }

                detail.setComputingCard(clusters.getCode().equals("QD")?"英伟达 * " +  cardNum : "晟腾 * " + cardNum);
                detail.setUsage("train".equals(params.getTaskType()) ? "模型训练" : "模型部署");
            }
        }
        return OperateResult.success(detail);
    }

    private String buildParamStr(String clusterCode, ClusterMetricVO clusterMetric, Map<String, List<String>> nodeCardMap) {
        StringBuilder paramStr = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : nodeCardMap.entrySet()) {
            if("QD".equalsIgnoreCase(clusterCode)){
                paramStr.append(clusterMetric.getExpression() + "{instance=" + "\"" + entry.getKey() + "\"" + ",uuid=~" + "\"" + String.join("|", entry.getValue()) + "\"" + "} or ");
            }else if("GZ".equalsIgnoreCase(clusterCode)){
                paramStr.append(clusterMetric.getExpression() + "{instance=" + "\"" + entry.getKey() + "\"" + ",id=~" + "\"" + String.join("|", entry.getValue()) + "\"" + "} or ");
            }
        }
        return paramStr.length() > 0? paramStr.substring(0, paramStr.lastIndexOf("}") + 1) : "";
    }

    private void handleMetricValue(String entryField, List<MetricValue<String>> metricValueList, ResourceUsageDetail detail, String clusterCode) {
        if ("gpuMemoryUsed".equalsIgnoreCase(entryField)) {
            long value = metricValueList.stream()
                    .map(metricValue -> {
                        String tempValue = metricValue.getValue();
                        if (NumberUtil.isNumber(tempValue)) {
                            return Convert.toInt(tempValue, 0);
                        }
                        log.error("无法将字符串转换为整数：{}", tempValue);
                        return 0;
                    })
                    .reduce(0, Integer::sum).longValue();
            if ("GZ".equals(clusterCode)) {
                value *= 1024*1024;
            }
            detail.setGpuMemoryUsed(ModelUtil.bytesToGB(value, 2)  + "G");
//            detail.setGpuMemoryUsed(ModelUtil.bytesToGB(metricValueList.stream()
//                    .map(metricValue -> {
//                        try {
//                            return Integer.parseInt(metricValue.getValue());
//                        } catch (NumberFormatException e) {
//                            log.error("无法将字符串转换为整数：" + metricValue.getValue(), e);
//                            return 0;
//                        }
//                    })
//                    .reduce(0, Integer::sum).longValue()));
        } else if ("gpuMemoryTotal".equalsIgnoreCase(entryField)) {
//            detail.setGpuMemoryTotal(ModelUtil.bytesToGB(metricValueList.stream()
//                    .map(metricValue -> {
//                        try {
//                            return Integer.parseInt(metricValue.getValue());
//                        } catch (NumberFormatException e) {
//                            log.error("无法将字符串转换为整数：" + metricValue.getValue(), e);
//                            return 0;
//                        }
//                    })
//                    .reduce(0, Integer::sum).longValue()));
            long value = metricValueList.stream()
                    .map(metricValue -> {
                        String tempValue = metricValue.getValue();
                        if (NumberUtil.isNumber(tempValue)) {
                            return Convert.toInt(tempValue, 0);
                        }
                        log.error("无法将字符串转换为整数：{}", tempValue);
                        return 0;
                    })
                    .reduce(0, Integer::sum).longValue();
            if ("GZ".equals(clusterCode)) {
                value *= 1024*1024;
            }
            detail.setGpuMemoryTotal(ModelUtil.bytesToGB(value, 2) + "G");
        } else if ("gpuTemperature".equalsIgnoreCase(entryField)) {
//            detail.setGpuTemperature(metricValueList.stream()
//                    .map(MetricValue::getValue)
//                    .reduce("", (a, b) -> a.isEmpty()? b : a + "," + b));
            String value = metricValueList.stream()
                    .map(MetricValue::getValue)
                    .reduce("", (a, b) -> a.isEmpty()? b : a + "," + b);
            if (!value.isEmpty()) {
                String[] temperatures = value.split(",");
                StringBuilder result = new StringBuilder();
                for (String temperature : temperatures) {
                    result.append(temperature).append("℃,");
                }
                detail.setGpuTemperature(result.substring(0, result.length() - 1));
            } else {
                detail.setGpuTemperature("");
            }
        } else if ("gpuPowerUsage".equalsIgnoreCase(entryField)) {
//            detail.setGpuPowerUsage(metricValueList.stream()
//                    .map(MetricValue::getValue)
//                    .reduce("", (a, b) -> a.isEmpty()? b : a + "," + b));
            String value = metricValueList.stream()
                    .map(MetricValue::getValue)
                    .reduce("", (a, b) -> a.isEmpty()? b : a + "," + b);
            if (!value.isEmpty()) {
                String[] powerUsages = value.split(",");
                StringBuilder result = new StringBuilder();
                for (String powerUsage : powerUsages) {
                    try {
                        double usage = Double.parseDouble(powerUsage);
                        if("QD".equals(clusterCode)){
                            usage /= 1000;
                        }
                        DecimalFormat df = new DecimalFormat("0.00");
                        result.append(df.format(usage)).append("W,");
                    } catch (NumberFormatException e) {
                        log.error("无法将字符串转换为双精度浮点数：" + powerUsage, e);
                        result.append(powerUsage).append(",");
                    }
//                    if ("QD".equals(clusterCode)) {
//                    } else {
//                        result.append(powerUsage).append("W,");
//                    }
                }
                detail.setGpuPowerUsage(result.substring(0, result.length() - 1));
            } else {
                detail.setGpuPowerUsage("");
            }
        }
    }
}