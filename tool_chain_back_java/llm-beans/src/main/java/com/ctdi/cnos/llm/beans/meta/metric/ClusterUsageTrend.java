package com.ctdi.cnos.llm.beans.meta.metric;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.List;

/**
 * 集群资源使用趋势。
 *
 * @author laiqi
 * @since 2024/9/23
 */
@FieldNameConstants
@ApiModel(description = "集群资源使用趋势")
@Data
@Accessors(chain = true)
public class ClusterUsageTrend {

    @ApiModelProperty(value = "CPU使用量")
    private List<MetricValue<String>> cpuUsed;

    @ApiModelProperty(value = "GPU使用量")
    private List<MetricValue<String>> gpuUsed;

    @ApiModelProperty(value = "内存使用量")
    private List<MetricValue<String>> memoryUsed;

    @ApiModelProperty(value = "存储使用量")
    private List<MetricValue<String>> storageUsed;

    /* @ApiModelProperty(value = "CPU使用量单位")
    private String cpuUsedUnit;

    @ApiModelProperty(value = "GPU使用量单位")
    private String gpuUsedUnit;

    @ApiModelProperty(value = "内存使用量单位")
    private String memoryUsedUnit;

    @ApiModelProperty(value = "存储使用量单位")
    private String storageUsedUnit; */

}