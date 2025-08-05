package com.ctdi.cnos.llm.metadata.extra.cluster.processor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetric;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetricVO;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterVO;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterResourceStatItem;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterStatParam;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterUsageTrend;
import com.ctdi.cnos.llm.beans.meta.metric.MetricValue;
import com.ctdi.cnos.llm.exception.MyRuntimeException;
import com.ctdi.cnos.llm.metadata.client.MetricApiClient;
import com.ctdi.cnos.llm.metadata.extra.cluster.BaseClusterResourceStatItemProcessor;
import com.ctdi.cnos.llm.metadata.service.ClusterMetricService;
import com.ctdi.cnos.llm.metadata.service.ClusterService;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.ModelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 集群资源使用趋势。
 *
 * @author laiqi
 * @since 2024/9/23
 */
@Slf4j
@Component
public class ClusterUsageTrendProcessor extends BaseClusterResourceStatItemProcessor {

    private final ClusterMetricService clusterMetricService;

    private final ClusterService clusterService;
    private final MetricApiClient metricApiClient;

    public ClusterUsageTrendProcessor(ClusterMetricService clusterMetricService, ClusterService clusterService, MetricApiClient metricApiClient) {
        super(ClusterResourceStatItem.CLUSTER_USAGE_TREND);
        this.clusterMetricService = clusterMetricService;
        this.clusterService = clusterService;
        this.metricApiClient = metricApiClient;
    }

    @Override
    protected OperateResult<?> execute(ClusterStatParam params) {
        ClusterUsageTrend clusterUsageTrend = new ClusterUsageTrend();
        // 参数验证
        if (Objects.nonNull(params) && params.getUsageTrendDuration() != null) {
            List<ClusterVO> clusterList = new ArrayList<>();
            // 如果指定了集群编码，则只查询该集群
            if (CharSequenceUtil.isNotBlank(params.getClusterCode())) {
                ClusterVO cluster = clusterService.queryByCode(params.getClusterCode());
                if (cluster != null) {
                    clusterList.add(cluster);
                }
            } else {
                clusterList = clusterService.queryList(new QueryParam());
            }
            int clusterSize = clusterList.size();
            Map<String, ClusterVO> clusterMap = clusterList.stream()
                    .collect(Collectors.toMap(ClusterVO::getCode, cluster -> cluster));
            QueryParam queryParam = new QueryParam();
            // 查询指定类别的集群指标
            ClusterMetric filterDto = new ClusterMetric();
            filterDto.setCategory(ClusterResourceStatItem.CLUSTER_USAGE_TREND.name());
            // filterDto.setEntryField("cpuUsed");
            queryParam.setFilterDto(filterDto);
            List<ClusterMetricVO> clusterMetrics = clusterMetricService.queryList(queryParam);
            // 先按实体字段分组、再按集群编码分组
            Map<String, Map<String, List<ClusterMetricVO>>> fieldClusterMetricGroup = clusterMetrics.stream().collect(
                    // 按entryField分组
                    Collectors.groupingBy(ClusterMetricVO::getEntryField,
                            // 按集群编码分组
                            Collectors.groupingBy(ClusterMetricVO::getClusterCode))
            );
            Integer usageTrendDuration = params.getUsageTrendDuration();
            // 步长
            String step = params.getUsageTrendDuration() == 1 ? "5m" : "1h";
            // 开始时间
            DateTime start = DateUtil.offsetDay(new Date(), -usageTrendDuration);
            // 结束时间
            DateTime end = DateUtil.date();
            for (Map.Entry<String, Map<String, List<ClusterMetricVO>>> entryFieldGroup : fieldClusterMetricGroup.entrySet()) {
                // 指标字段
                String entryField = entryFieldGroup.getKey();
                // 指标值(给前端的)
                List<MetricValue<Double>> entryFieldValue;
                String entryFieldUnit = "";
                // 遍历处理集群的指标获取
                Map<String, List<ClusterMetricVO>> clusterMetricGroup = entryFieldGroup.getValue();
                List<List<MetricValue<Double>>> clusterMetricData = new ArrayList<>(clusterSize);
                try {
                    for (Map.Entry<String, List<ClusterMetricVO>> clusterGroup : clusterMetricGroup.entrySet()) {
                        String clusterCode = clusterGroup.getKey();
                        if (clusterMap.containsKey(clusterCode)) {
                            ClusterVO cluster = clusterMap.get(clusterCode);
                            List<ClusterMetricVO> clusterMetricList = clusterGroup.getValue();
                            entryFieldUnit = CollUtil.getFirst(clusterMetricList).getUnit();
                            for (ClusterMetricVO clusterMetric : clusterMetricList) {
                                String expression = clusterMetric.getExpression();
                                List<MetricValue<Double>> metricValues = metricApiClient.queryRange(expression, start, end, step, cluster.getPrometheusHostUrl(), Double.class);
                                Console.log("查询集群：{}, 指标名：{}, 表达式：{}, 指标单位：{}, 返回指标数量：{}", clusterMetric.getClusterCode(), clusterMetric.getName(), expression, entryFieldUnit, metricValues.size());
                                Console.log(JSON.toJSONString(metricValues));
                                clusterMetricData.add(metricValues);
                            }
                        }
                    }
                    // 指定了集群
                    if (clusterMetricData.size() == 1) {
                        entryFieldValue = clusterMetricData.get(0);
                    }
                    // 指定了二个集群
                    else if (clusterMetricData.size() == 2) {
                        entryFieldValue = MetricApiClient.aggregateValues(clusterMetricData.get(0), clusterMetricData.get(1), NumberUtil::add);
                    }
                    else {
                        throw new MyRuntimeException("暂未支持处理2个以上的集群。");
                    }
                    ReflectUtil.setFieldValue(clusterUsageTrend, entryField, entryFieldValue.stream().map(item -> {
                        MetricValue<String> metricValue = new MetricValue<>();
                        metricValue.setTime(item.getTime());
                        String newValue = ClusterUsageTrend.Fields.cpuUsed.equalsIgnoreCase(entryField) ? item.getValue().toString(): ModelUtil.bytesToGB(item.getValue().longValue());
                        metricValue.setValue(newValue);
                        return metricValue;
                    }).collect(Collectors.toList()));
                    // ReflectUtil.setFieldValue(clusterUsageTrend, entryField + "Unit", entryFieldUnit);
                } catch (Exception e) {
                    log.error("查询集群指标异常:{}, {}", entryField, e.getMessage(), e);
                }
            }
        }
        return OperateResult.success(clusterUsageTrend);
    }
}