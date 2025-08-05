package com.ctdi.cnos.llm.beans.meta.metric;

/**
 * 集群资源汇总项。
 *
 * @author laiqi
 * @since 2024/9/23
 */
public enum ClusterResourceStatItem {
    /**
     * 集群资源计数
     */
    RESOURCE_COUNT,
    /**
     * 集群资源使用情况
     */
    RESOURCE_USAGE,
    /**
     * 集群详情
     */
    CLUSTER_DETAIL,
    /**
     * 集群使用趋势
     */
    CLUSTER_USAGE_TREND,
    /**
     * 资源使用明细
     */
    RESOURCE_USAGE_DETAIL;
}