/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.model;

import com.alibaba.fastjson.JSONArray;
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
 * 基础模型超参
 * 注意与train.TrainTaskParamVO 的参数名一致
 *
 * @author huangjinhua
 * @since 2024/5/14
 */
@Data
@ApiModel("模型超参")
@TableName("meta.mm_model_train_param")
public class ModelTrainParam implements Serializable {
    private static final long serialVersionUID = -12685702896326562L;
    @ApiModelProperty(value = "ID，修改时必填!", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "模型训练ID，mm_model_train.id", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("model_train_id")
    private Long modelTrainId;

    @ApiModelProperty(value = "前端显示名称", example = "迭代轮次")
    @TableField("display_name")
    private String displayName;
    @ApiModelProperty(value = "后端使用的键名", example = "custom_epoch")
    @TableField("be_key")
    private String beKey;
    @ApiModelProperty(value = "前端使用的键名", example = "epoch")
    @TableField("fe_key")
    private String feKey;
    @ApiModelProperty(value = "参数类型", example = "int")
    @TableField("\"type\"")
    private String type;
    @ApiModelProperty(value = "分组信息", example = "")
    @TableField("\"group\"")
    private String group;
    @ApiModelProperty(value = "校验类型", example = "range")
    @TableField("check_type")
    private String checkType;
    @ApiModelProperty(value = "校验值", example = "[1, 50]")
    @TableField(exist = false)
    private JSONArray checkValue;
    @ApiModelProperty(value = "校验值,用于数据库存储", example = "[1, 50]", hidden = true)
    @TableField("check_value")
    private String checkValueStr;
    @ApiModelProperty(value = "步长值", example = "1")
    @TableField("step")
    private Float step;
    @ApiModelProperty(value = "默认值", example = "1")
    @TableField(exist = false)
    private Object defaultValue;
    @ApiModelProperty(value = "默认值,用于数据库存储", example = "1", hidden = true)
    @TableField("default_value")
    private String defaultValueStr;
    @ApiModelProperty(value = "描述", example = "通用能力优异，适合作为基座模型进行精调，更好地处理特定场景问题，同时具备极佳的推理性能")
    @TableField("description")
    private String description;

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
