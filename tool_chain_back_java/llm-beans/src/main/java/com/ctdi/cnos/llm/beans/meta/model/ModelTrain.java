/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 模型训练配置
 *
 * @author huangjinhua
 * @since 2024/7/3
 */
@Data
@ApiModel("模型训练配置")
@TableName("meta.mm_model_train")
public class ModelTrain implements Serializable {
    private static final long serialVersionUID = -12685702896326562L;
    @ApiModelProperty(value = "ID，修改时必填!", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "模型ID，mm_model.id", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("model_id")
    private Long modelId;

    @ApiModelProperty(value = "类型，有model.id的为specifical（特定的），没有的为normal（通用的）", example = "1775389563611901951")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "训练方法，详见字典TRAIN_TASK_METHOD", example = "1")
    @TableField("train_method")
    private String trainMethod;
    @ApiModelProperty(value = "训练类型，详见字典MODEL_TRAIN_TYPE", example = "1")
    @TableField("train_type")
    private String trainType;
    @ApiModelProperty(value = "创建人", example = "-1", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("creator_id")
    private Long creatorId;
    @ApiModelProperty(value = "创建时间", example = "2024-04-01 08:00:00", hidden = true)
    @TableField("create_date")
    private Date createDate;
    @ApiModelProperty(value = "更新人", example = "-1", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("modifier_id")
    private Long modifierId;
    @ApiModelProperty(value = "更新时间", example = "2024-04-01 08:10:00", hidden = true)
    @TableField("modify_date")
    private Date modifyDate;

}
