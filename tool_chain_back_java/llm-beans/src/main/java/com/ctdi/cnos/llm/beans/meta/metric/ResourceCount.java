package com.ctdi.cnos.llm.beans.meta.metric;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 集群资源计数。
 *
 * @author laiqi
 * @since 2024/9/23
 */
@ApiModel(description = "集群资源计数")
@Data
@Accessors(chain = true)
public class ResourceCount {
    @ApiModelProperty(value = "集群数量")
    private Integer clusterCount;

    @ApiModelProperty(value = "服务器数量")
    private Integer serverCount;

    @ApiModelProperty(value = "POD数量")
    private Integer podCount;

    @ApiModelProperty(value = "可用算力卡数量")
    private Integer availableCardCount;
}