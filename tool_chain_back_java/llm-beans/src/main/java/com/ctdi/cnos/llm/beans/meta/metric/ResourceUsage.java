package com.ctdi.cnos.llm.beans.meta.metric;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 集群资源使用情况。
 *
 * @author laiqi
 * @since 2024/9/23
 */
public @ApiModel(description = "集群资源使用情况")
@Data
@Accessors(chain = true)
class ResourceUsage {

    @ApiModelProperty(value = "CPU总量")
    private Double cpuTotal;
    @ApiModelProperty(value = "CPU使用量")
    private Double cpuUsed;

    @ApiModelProperty(value = "CPU剩余量")
    private Double cpuFree;

    @ApiModelProperty(value = "GPU总量")
    private Double gpuTotal;
    @ApiModelProperty(value = "GPU使用量")
    private Double gpuUsed;

    @ApiModelProperty(value = "GPU剩余量")
    private Double gpuFree;

    @ApiModelProperty(value = "内存总量")
    private Double memoryTotal;

    @ApiModelProperty(value = "内存使用量")
    private Double memoryUsed;

    @ApiModelProperty(value = "内存剩余量")
    private Double memoryFree;

    @ApiModelProperty(value = "存储总量")
    private Double storageTotal;

    @ApiModelProperty(value = "存储使用量")
    private Double storageUsed;

    @ApiModelProperty(value = "存储剩余量")
    private Double storageFree;

    @ApiModelProperty(value = "服务器总量")
    private Integer serverTotal;

    @ApiModelProperty(value = "服务器已用数")
    private Integer serverUsed;

    @ApiModelProperty(value = "服务器可用数")
    private Integer serverFree;
}