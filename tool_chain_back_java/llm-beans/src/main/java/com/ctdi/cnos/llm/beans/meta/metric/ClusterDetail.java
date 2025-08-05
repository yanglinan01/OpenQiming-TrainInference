package com.ctdi.cnos.llm.beans.meta.metric;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 集群详情。
 *
 * @author laiqi
 * @since 2024/9/23
 */
@ApiModel(description = "集群详情")
@Data
@Accessors(chain = true)
public class ClusterDetail {

    @ApiModelProperty(value = "集群名称")
    private String clusterName;

    @ApiModelProperty(value = "省份")
    private String province;

    /*@ApiModelProperty(value = "GPU利用率")
    private Double gpuUsage;*/

    @ApiModelProperty(value = "CPU利用率")
    private Double cpuUsage;

    @ApiModelProperty(value = "内存利用率")
    private Double memoryUsage;

    @ApiModelProperty(value = "训练服务器剩余量")
    private Integer trainServerAvail;

    @ApiModelProperty(value = "推理服务器剩余量")
    private Integer inferenceServerAvail;
}