package com.ctdi.cnos.llm.beans.train.deployTask;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/12 10:09
 * @Description
 */
@Data
@ApiModel("部署任务回调")
public class DeployTaskCallback {
    @ApiModelProperty(value = "ID，修改时必填!", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "任务状态，字典DEPLOY_TASK_STATUS", example = "")
    private String status;

    @ApiModelProperty(value = "部署地址url", example = "")
    private String deployUrl;

    @ApiModelProperty(value = "部署消息", example = "")
    private String result;
}
