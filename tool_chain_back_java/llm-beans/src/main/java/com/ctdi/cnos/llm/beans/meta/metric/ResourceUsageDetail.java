package com.ctdi.cnos.llm.beans.meta.metric;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yuyong
 * @date 2024/9/25 13:51
 */
@ApiModel(description = "使用资源明细")
@Data
@Accessors(chain = true)
public class ResourceUsageDetail {

    @ApiModelProperty(value = "集群名称")
    private String clusterName;

    @ApiModelProperty(value = "用途")
    private String usage;

    @ApiModelProperty(value = "算力卡")
    private String computingCard;

    @ApiModelProperty(value = "GPU显存使用量")
    private String gpuMemoryUsed;

    @ApiModelProperty(value = "GPU显存总量")
    private String gpuMemoryTotal;

    @ApiModelProperty(value = "GPU显卡温度")
    private String gpuTemperature;

    @ApiModelProperty(value = "GPU功率")
    private String gpuPowerUsage;

}
