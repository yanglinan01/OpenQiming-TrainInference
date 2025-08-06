package com.ctdi.cnos.llm.metadata.extra.cluster.processor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetric;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetricVO;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterVO;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterResourceStatItem;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterStatParam;
import com.ctdi.cnos.llm.beans.meta.metric.ResourceUsage;
import com.ctdi.cnos.llm.metadata.client.MetricApiClient;
import com.ctdi.cnos.llm.metadata.extra.cluster.BaseClusterResourceStatItemProcessor;
import com.ctdi.cnos.llm.metadata.service.ClusterMetricService;
import com.ctdi.cnos.llm.metadata.service.ClusterService;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.ModelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 集群资源使用情况。
 *
 * @author laiqi
 * @since 2024/9/23
 */
@Component
@Slf4j
public class ResourceUsageProcessor extends BaseClusterResourceStatItemProcessor {

    private ClusterService clusterService;
    private ClusterMetricService clusterMetricService;
    private MetricApiClient metricApiClient;

    public ResourceUsageProcessor(ClusterService clusterService, ClusterMetricService clusterMetricService, MetricApiClient metricApiClient) {
        super(ClusterResourceStatItem.RESOURCE_USAGE);
        this.clusterService = clusterService;
        this.clusterMetricService = clusterMetricService;
        this.metricApiClient = metricApiClient;
    }

    @Override
    protected OperateResult<?> execute(ClusterStatParam params) {
        //总结果
        ResourceUsage resourceUsage = new ResourceUsage();
        //获取集群信息
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
            List<ResourceUsage> resourceItemList = new ArrayList<>();
            for (ClusterVO cluster : clusterList) {
                QueryParam queryParam = new QueryParam();
                ClusterMetric clusterMetricQuery = new ClusterMetric();
                clusterMetricQuery.setClusterCode(cluster.getCode());
                clusterMetricQuery.setCategory(super.getType().name());
                queryParam.setFilterDto(clusterMetricQuery);

                List<ClusterMetricVO> clusterMetricList = clusterMetricService.queryList(queryParam);
                List<String> metricList = clusterMetricList.stream()
                        .map(ClusterMetricVO::getExpression)
                        .collect(Collectors.toList());
                //以集群维度或者指标值，指标都是单值的
                Map<String, String> valueMap = Optional.of(metricList)
                        .map(list -> metricApiClient.queryMultiMetricValue(list, cluster.getPrometheusHostUrl(), String.class))
                        .orElse(MapUtil.empty());

                log.info("{}的采集数据为：{}", cluster.getName(), JSON.toJSONString(valueMap));

                ResourceUsage resourceItem = new ResourceUsage();
                for (ClusterMetricVO metricVO : clusterMetricList) {
                    //反射采集值
                    String value = valueMap.get(metricVO.getExpression());
                    //设置默认值
                    if (CharSequenceUtil.isBlank(value)) {
                        value = "0";
                    }
                    ReflectUtil.setFieldValue(resourceItem, metricVO.getEntryField(), value);
                }
                //计算使用值
                this.fixUsed(resourceItem);
                resourceItemList.add(resourceItem);
                log.info("{}映射后的对象为：{}", cluster.getName(), JSON.toJSONString(resourceItem));
            }

            //多集群相同属性相加
            resourceUsage = this.fieldSum(resourceItemList);
            log.info("结果对象为：{}", JSON.toJSONString(resourceUsage));

            //单位转换B->GB
            this.unitToGB(resourceUsage);
        }
        log.info("单位转换后的结果对象为：{}", JSON.toJSONString(resourceUsage));
        return OperateResult.success(resourceUsage);
    }

    /**
     * 填充使用量
     *
     * @param resourceItem 单个集群的采集对象
     */
    private void fixUsed(ResourceUsage resourceItem) {
        //计算使用值
        if (ObjectUtil.isNull(resourceItem.getGpuUsed())) {

            resourceItem.setGpuUsed(NumberUtil.sub(resourceItem.getGpuTotal(), resourceItem.getGpuFree()));
        }
        if (ObjectUtil.isNull(resourceItem.getCpuUsed())) {
            resourceItem.setCpuUsed(NumberUtil.sub(resourceItem.getCpuTotal(), resourceItem.getCpuFree()));
        }
        if (ObjectUtil.isNull(resourceItem.getMemoryUsed())) {
            resourceItem.setMemoryUsed(NumberUtil.sub(resourceItem.getMemoryTotal(), resourceItem.getMemoryFree()));
        }
        if (ObjectUtil.isNull(resourceItem.getStorageUsed())) {
            resourceItem.setStorageUsed(NumberUtil.sub(resourceItem.getStorageTotal(), resourceItem.getStorageFree()));
        }
        if (ObjectUtil.isNull(resourceItem.getServerUsed())) {
            resourceItem.setServerUsed(NumberUtil.sub(resourceItem.getServerTotal(), resourceItem.getServerFree()).intValue());
        }
    }


    /**
     * 集群值聚合(多集群相同属性相加)
     *
     * @param resourceItemList 每个集群的采集对象
     */
    private ResourceUsage fieldSum(List<ResourceUsage> resourceItemList) {
        ResourceUsage resourceUsage = new ResourceUsage();
        //多集群相同属性相加
        if (CollUtil.isNotEmpty(resourceItemList)) {
            //单集群直接返回
            resourceUsage = resourceItemList.stream().reduce((item1, item2) -> {
                ResourceUsage item = new ResourceUsage();
                item.setGpuTotal(NumberUtil.add(item1.getGpuTotal(), item2.getGpuTotal()));
                item.setGpuTotal(NumberUtil.add(item1.getGpuTotal(), item2.getGpuTotal()));
                item.setGpuFree(NumberUtil.add(item1.getGpuFree(), item2.getGpuFree()));
                item.setGpuUsed(NumberUtil.add(item1.getGpuUsed(), item2.getGpuUsed()));

                item.setCpuTotal(NumberUtil.add(item1.getCpuTotal(), item2.getCpuTotal()));
                item.setCpuFree(NumberUtil.add(item1.getCpuFree(), item2.getCpuFree()));
                item.setCpuUsed(NumberUtil.add(item1.getCpuUsed(), item2.getCpuUsed()));

                item.setMemoryTotal(NumberUtil.add(item1.getMemoryTotal(), item2.getMemoryTotal()));
                item.setMemoryFree(NumberUtil.add(item1.getMemoryFree(), item2.getMemoryFree()));
                item.setMemoryUsed(NumberUtil.add(item1.getMemoryUsed(), item2.getMemoryUsed()));

                item.setStorageTotal(NumberUtil.add(item1.getStorageTotal(), item2.getStorageTotal()));
                item.setStorageFree(NumberUtil.add(item1.getStorageFree(), item2.getStorageFree()));
                item.setStorageUsed(NumberUtil.add(item1.getStorageUsed(), item2.getStorageUsed()));

                item.setServerTotal(NumberUtil.add(item1.getServerTotal(), item2.getServerTotal()).intValue());
                item.setServerFree(NumberUtil.add(item1.getServerFree(), item2.getServerFree()).intValue());
                item.setServerUsed(NumberUtil.add(item1.getServerUsed(), item2.getServerUsed()).intValue());

                return item;
            }).orElse(null);
        }
        return resourceUsage;
    }

    /**
     * 单位转换B->GB
     *
     * @param resourceUsage 采集对象
     */
    private void unitToGB(ResourceUsage resourceUsage) {
        //单位转换B->GB
        resourceUsage.setGpuTotal(ModelUtil.bytesToGB(resourceUsage.getGpuTotal(), 2));
        resourceUsage.setGpuFree(ModelUtil.bytesToGB(resourceUsage.getGpuFree(), 2));
        resourceUsage.setGpuUsed(ModelUtil.bytesToGB(resourceUsage.getGpuUsed(), 2));

        resourceUsage.setMemoryTotal(ModelUtil.bytesToGB(resourceUsage.getMemoryTotal(), 2));
        resourceUsage.setMemoryFree(ModelUtil.bytesToGB(resourceUsage.getMemoryFree(), 2));
        resourceUsage.setMemoryUsed(ModelUtil.bytesToGB(resourceUsage.getMemoryUsed(), 2));

        resourceUsage.setStorageTotal(ModelUtil.bytesToGB(resourceUsage.getStorageTotal(), 2));
        resourceUsage.setStorageFree(ModelUtil.bytesToGB(resourceUsage.getStorageFree(), 2));
        resourceUsage.setStorageUsed(ModelUtil.bytesToGB(resourceUsage.getStorageUsed(), 2));

    }
}