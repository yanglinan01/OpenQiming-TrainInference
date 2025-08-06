/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.train.trainTask;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 训练任务发布
 *
 * @author wangyy
 * @since 2024-12-18
 */
@Data
@ApiModel("训练任务发布")
public class TrainTaskPublishReq implements Serializable {

    @ApiModelProperty(value = "modelId", example = "1775389563611901951", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "发布到(0 模型广场,1 项目空间)", required = true, example = "0")
    private String publishTo;

    @ApiModelProperty(value = "模型描述", required = true, example = "")
    private String modelDesc;

    @ApiModelProperty(value = "出入参(模型广场必传)", example = "")
    private String modelParams;

    @ApiModelProperty(value = "模型名称(模型广场必传)", example = "")
    private String modelName;

    @ApiModelProperty(value = "项目空间id(项目空间必传)", example = "")
    private String projectSpaceId;
}
