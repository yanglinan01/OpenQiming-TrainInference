package com.ctdi.cnos.llm.beans.train;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author wangyb
 * @date 2024/9/6 0006 9:14
 * @description DeployRegistryReq
 */
@Data
@ApiModel("deployRegistryReq")
public class DeployRegistryReq {

    @ApiModelProperty(value = "部署任务id", example = "123123", required = true)
    @NotNull(message = "数据验证失败, deployTaskId不能为空！")
    private String deployTaskId;

    @ApiModelProperty(value = "状态(1上线;0下线)", example = "1", required = true)
    @NotNull(message = "数据验证失败, status不能为空！")
    private String status;

}
