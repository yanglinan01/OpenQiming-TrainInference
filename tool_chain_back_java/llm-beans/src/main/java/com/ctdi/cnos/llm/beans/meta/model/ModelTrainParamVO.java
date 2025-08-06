/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 模型
 *
 * @author huangjinhua
 * @since 2024/7/1
 */
@Data
@ApiModel("模型VO")
public class ModelTrainParamVO extends ModelTrainParam {


    @ApiModelProperty(value = "类型，有model.id的为specifical（特定的），没有的为normal（通用的）", example = "1")
    private String type;

    @ApiModelProperty(value = "训练方法，详见字典TRAIN_TASK_METHOD", example = "1")
    private String trainMethod;

    @ApiModelProperty(value = "训练类型，详见字典MODEL_TRAIN_TYPE", example = "SFT")
    private String trainType;

    @ApiModelProperty(value = "模型ID，mm_model.id", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modelId;
}
