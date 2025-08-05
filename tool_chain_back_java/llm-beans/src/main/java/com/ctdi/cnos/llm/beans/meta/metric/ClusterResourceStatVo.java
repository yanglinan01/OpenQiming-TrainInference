package com.ctdi.cnos.llm.beans.meta.metric;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 集群资源汇总信息对象。
 *
 * @author laiqi
 * @since 2024/9/23
 */
@ApiModel(description = "集群资源汇总信息对象")
@Data
@Accessors(chain = true)
public class ClusterResourceStatVo {

    /**
     * 集群资源计数。
     */
    private ResourceCount resourceCounts;

    /**
     * 集群资源使用情况。
     */
    private ResourceUsage resourceUsage;

    /**
     * 集群详情。
     */
    private List<ClusterDetail> clusterDetails;

}