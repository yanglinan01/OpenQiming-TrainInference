/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.application;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用广场
 *
 * @author huangjinhua
 * @since 2024/6/11
 */
@Getter
@Setter
@ApiModel("应用广场")
@TableName("meta.mm_application_square")
@Accessors(chain = true)
public class ApplicationSquare implements Serializable {

    private static final long serialVersionUID = -12685702896326562L;
    @ApiModelProperty(value = "ID，修改时必填!", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId("id")
    private Long id;
    @ApiModelProperty(value = "名称", example = "启明大模型")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "应用类型,字典 APPLICATION_TYPE", example = "1", required = true)
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "应用类型,字典 APPLICATION_BELONG", example = "1", required = true)
    @TableField("belong")
    private String belong;

    @ApiModelProperty(value = "场景, 字典APPLICATION_SCENE", example = "8")
    @TableField("scene")
    private String scene;
    @ApiModelProperty(value = "接口地址", example = "https://aip.com/ai_custom/v1/chat/qiming-base")
    @TableField("endpoint")
    private String endpoint;

    @ApiModelProperty(value = "是否可体验（0是，1否）", example = "0")
    @TableField("experience")
    private String experience;

    @ApiModelProperty(value = "身份识别码（dcoos平台接口时为数据EOP平台的识别码）", example = "8")
    @TableField("uid")
    private String uid;
    @ApiModelProperty(value = "描述", example = "通用能力优异，适合作为基座模型进行精调，更好地处理特定场景问题，同时具备极佳的推理性能")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "创建时间", example = "2024-04-01 08:00:00", hidden = true)
    @TableField("create_date")
    @JsonIgnore
    private Date createDate;
    @ApiModelProperty(value = "创建人", example = "-1", hidden = true)
    @JsonIgnore
    @TableField("creator_id")
    private Long creatorId;
    @ApiModelProperty(value = "更新人", example = "-1", hidden = true)
    @TableField("modifier_id")
    @JsonIgnore
    private Long modifierId;
    @ApiModelProperty(value = "更新时间", example = "2024-04-01 08:10:00", hidden = true)
    @TableField("modify_date")
    @JsonIgnore
    private Date modifyDate;
}