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
import com.ctdi.cnos.llm.beans.meta.metric.ClusterResourceStatItem;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterStatParam;
import com.ctdi.cnos.llm.beans.meta.metric.ResourceCount;
import com.ctdi.cnos.llm.metadata.client.MetricApiClient;
import com.ctdi.cnos.llm.metadata.extra.cluster.BaseClusterResourceStatItemProcessor;
import com.ctdi.cnos.llm.metadata.service.ClusterMetricService;
import com.ctdi.cnos.llm.metadata.service.ClusterService;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 集群资源计数。
 *
 * @author laiqi
 * @since 2024/9/23
 */
@Component
public class ResourceCountProcessor extends BaseClusterResourceStatItemProcessor {

    private final ClusterService clusterService;
    private final ClusterMetricService clusterMetricService;
    private final MetricApiClient metricApiClient;

    public ResourceCountProcessor(ClusterService clusterService, ClusterMetricService clusterMetricService, MetricApiClient metricApiClient) {
        super(ClusterResourceStatItem.RESOURCE_COUNT);
        this.clusterService = clusterService;
        this.clusterMetricService = clusterMetricService;
        this.metricApiClient = metricApiClient;
    }

    @Override
    protected OperateResult<?> execute(ClusterStatParam params) {
        List<ResourceCount> resourceCounts = new LinkedList<>();
        ResourceCount resourceCountTotal = new ResourceCount();
        List<ClusterVO> clusterList = new ArrayList<>();
        if (params != null && CharSequenceUtil.isNotBlank(params.getClusterCode())) {
            ClusterVO cluster = clusterService.queryByCode(params.getClusterCode());
            if (cluster != null) {
                clusterList.add(cluster);
            }
        } else {
            //获取集群信息
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
                    //获取所有查询表达式
                    List<String> metricSingleList = clusterMetricList.stream()
                            .filter(vo -> SystemConstant.NO.equals(vo.getResultList()))
                            .map(ClusterMetricVO::getExpression)
                            .collect(Collectors.toList());

                    //获取请求值(聚合指标，指标-值 一对一)
                    Map<String, Integer> singleMap = Optional.of(metricSingleList)
                            .map(list -> metricApiClient.queryMultiMetricValue(list, cluster.getPrometheusHostUrl(), Integer.class))
                            .orElse(MapUtil.empty());

                    ResourceCount resourceCount = new ResourceCount();
                    for (ClusterMetricVO metric : clusterMetricList) {
                        //反射采集值
                        Integer value = Optional.ofNullable(singleMap.get(metric.getExpression())).orElse(0);
                        ReflectUtil.setFieldValue(resourceCount, metric.getEntryField(), value);
                    }
                    //获取服务器数量
//                    resourceCount.setServerCount(metricApiClient.queryNodeTrainAndInference(cluster.getNodeHostUrl()).size()); 统计node节点, 不包括master
//                    resourceCount.setServerCount(10);
                    resourceCounts.add(resourceCount);
                }
            }
        }

        //所有集群求和
        int serverCount = resourceCounts.stream().mapToInt(count-> Convert.toInt(count.getServerCount(),0)).sum();
        int availableCardCount = resourceCounts.stream().mapToInt(count-> Convert.toInt(count.getAvailableCardCount(),0)).sum();
        int podCount = resourceCounts.stream().mapToInt(count-> Convert.toInt(count.getPodCount(),0)).sum();
        resourceCountTotal.setClusterCount(clusterList.size()).setPodCount(podCount).setServerCount(serverCount).setAvailableCardCount(availableCardCount);
        return OperateResult.success(resourceCountTotal);
    }
}