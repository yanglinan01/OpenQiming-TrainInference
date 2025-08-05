package com.ctdi.cnos.llm.metadata.extra.cluster.processor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
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
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 集群资源使用情况。
 *
 * @author laiqi
 * @since 2024/9/23
 */
@Component
public class ClusterDetailProcessor extends BaseClusterResourceStatItemProcessor {

    private final ClusterService clusterService;
    private final ClusterMetricService clusterMetricService;
    private final MetricApiClient metricApiClient;

    public ClusterDetailProcessor(ClusterService clusterService, ClusterMetricService clusterMetricService, MetricApiClient metricApiClient) {
        super(ClusterResourceStatItem.CLUSTER_DETAIL);
        this.clusterService = clusterService;
        this.clusterMetricService = clusterMetricService;
        this.metricApiClient = metricApiClient;
    }

    @Override
    protected OperateResult<?> execute(ClusterStatParam params) {
        List<ClusterDetail> clusterDetails = new LinkedList<>();
        List<ClusterVO> clusterList = new ArrayList<>();
        if (params != null && CharSequenceUtil.isNotBlank(params.getClusterCode())) {
            ClusterVO cluster = clusterService.queryByCode(params.getClusterCode());
            if (cluster != null) {
                clusterList.add(cluster);
            }
        } else {
            clusterList = clusterService.queryList(new QueryParam());
        }
        if (CollUtil.isNotEmpty(clusterList)) {
            for (ClusterVO cluster : clusterList) {
                //查询对应绑定的指标
                QueryParam queryParam = new QueryParam();
                ClusterMetric clusterMetricQuery = new ClusterMetric();
                clusterMetricQuery.setClusterCode(cluster.getCode());
                clusterMetricQuery.setCategory(super.getType().name());
                queryParam.setFilterDto(clusterMetricQuery);
                List<ClusterMetricVO> clusterMetricList = clusterMetricService.queryList(queryParam);

                if (CollUtil.isNotEmpty(clusterMetricList)) {
                    List<String> metricList = clusterMetricList.stream()
                            .filter(vo -> SystemConstant.YES.equals(vo.getResultList()))
                            .map(ClusterMetricVO::getExpression)
                            .collect(Collectors.toList());
                    //请求接口(聚合指标，值为列表)
                    Map<String, List<MetricResult<String>>> listMap = Optional.of(metricList)
                            .map(list -> metricApiClient.queryMultiMetric(list, cluster.getPrometheusHostUrl(), String.class))
                            .orElse(MapUtil.empty());


                    List<String> metricSingleList = clusterMetricList.stream()
                            .filter(vo -> SystemConstant.NO.equals(vo.getResultList()))
                            .map(ClusterMetricVO::getExpression)
                            .collect(Collectors.toList());
                    //请求接口(聚合指标，指标-值 一对一)
                    Map<String, Double> singleMap = Optional.of(metricSingleList)
                            .map(list -> metricApiClient.queryMultiMetricValue(list, cluster.getPrometheusHostUrl(), Double.class))
                            .orElse(MapUtil.empty());

                    //映射对象
                    ClusterDetail detail = new ClusterDetail();
                    detail.setClusterName(cluster.getName());
                    detail.setProvince(cluster.getProvince());
                    for (ClusterMetricVO metric : clusterMetricList) {
                        if ("gpuCardAvail".equalsIgnoreCase(metric.getEntryField())) {
                            //获取节点用途
                            List<ClusterNode> clusterNodes = metricApiClient.queryNodeTrainAndInference(cluster.getNodeHostUrl());
                            //可用卡
                            List<MetricResult<String>> enableCard = listMap.get(metric.getExpression());
                            if (CollUtil.isNotEmpty(enableCard)) {
                                //获取可用卡的信息
                                List<String> cardNode = enableCard.stream().map(MetricResult::getInstance).collect(Collectors.toList());
                                //训练服务器剩余量
                                long countTrainAvail = clusterNodes.stream()
                                        .filter(node -> node.isTrain() && (CollUtil.contains(cardNode, node.getName()) || CollUtil.contains(cardNode, node.getIp() + ":9445")))
                                        .count();
                                //推理服务器剩余量
                                long countInferenceAvail = clusterNodes.stream()
                                        .filter(node -> node.isInference() && (CollUtil.contains(cardNode, node.getName()) || CollUtil.contains(cardNode, node.getIp() + ":9445")))
                                        .count();
                                //训练服务器余量
                                detail.setTrainServerAvail(Convert.toInt(countTrainAvail, 0));
                                //推理服务器余量
                                detail.setInferenceServerAvail(Convert.toInt(countInferenceAvail, 0));
                            }
                        } else {
                            //反射采集值
                            ReflectUtil.setFieldValue(detail, metric.getEntryField(), singleMap.get(metric.getExpression()));
                        }
                    }
                    clusterDetails.add(detail);
                }
            }
        }
        return OperateResult.success(clusterDetails);
    }
}