/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.train.deployTask;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部署回调智能体调用状态
 *
 * @author huangjinhua
 * @since 2024/9/4
 */
@Data
@ApiModel("部署任务回调智能体调用状态")
public class DeployTaskAgentStatusCallback {
    @ApiModelProperty(value = "部署任务ID", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deployTaskId;

    @ApiModelProperty(value = "是否被引用状态", example = "0")
    private boolean agentStatus;

    @ApiModelProperty(value = "引用状态(1是; 2否),(提供出去的接口参数)", example = "0")
    private String citedStatus;
}
