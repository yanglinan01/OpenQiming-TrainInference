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
 * 模型
 *
 * @author huangjinhua
 * @since 2024/7/1
 */
@Data
@ApiModel("模型")
@TableName("meta.mm_model")
public class Model implements Serializable {
    private static final long serialVersionUID = -12685702896326562L;
    @ApiModelProperty(value = "ID，修改时必填!", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId("id")
    private Long id;
    @ApiModelProperty(value = "名称", example = "Qming 14B")
    @TableField("name")
    private String name;
    @ApiModelProperty(value = "类型", example = "1")
    @TableField("type")
    private String type;
    @ApiModelProperty(value = "归属，详见字典MODEL_BELONG", example = "1")
    @TableField("belong")
    private String belong;

    @ApiModelProperty(value = "区域编码，meta.mm_province.code", example = "911010000000000000000000")
    @TableField("region_code")
    private String regionCode;

    @ApiModelProperty(value = "别名，用于大模型接口参数", example = "qiming14b")
    @TableField("alias")
    private String alias;
    @ApiModelProperty(value = "接口地址", example = "https://aip.com/ai_custom/v1/chat/qiming-base")
    @TableField("endpoint")
    private String endpoint;

    @ApiModelProperty(value = "身份识别码（dcoos平台接口时为数据EOP平台的识别码）", example = "8")
    @TableField("uid")
    private String uid;
    @ApiModelProperty(value = "停止标识符ID数组，用于控制输出停止的位置。多个逗号分隔。", example = "151645,151644,151643")
    @TableField("stop_token_ids")
    private String stopTokenIds;
    @ApiModelProperty(value = "采样策略参数，用于控制生成文本的多样性。", example = "-1")
    @TableField("top_k")
    private Integer topK;
    @ApiModelProperty(value = "控制生成文本的最大长度，单位是token数。", example = "512")
    @TableField("max_tokens")
    private Integer maxTokens;
    @ApiModelProperty(value = "是否可优化（0是，1否）", example = "0")
    @TableField("optimizable")
    private String optimizable;

    @ApiModelProperty(value = "是否可训练（0是，1否）", example = "0")
    @TableField("trainable")
    private String trainable;

    @ApiModelProperty(value = "训练接口目标，GZ：贵州，QD青岛", example = "GZ")
    @TableField("train_target")
    private String trainTarget;

    @ApiModelProperty(value = "是否可体验（0是，1否）", example = "0")
    @TableField("experience")
    private String experience;

    @ApiModelProperty(value = "体验形式，详见字典MODEL_EXPERIENCE_SHAPE", example = "0")
    @TableField("experience_shape")
    private String experienceShape;

    @ApiModelProperty(value = "体验实现，详见字典APPLICATION_TYPE", example = "1")
    @TableField("experience_impl")
    private Integer experienceImpl;

    @ApiModelProperty(value = "是否可推理（0是，1否）", example = "0")
    @TableField("reasonable")
    private String reasonable;

    @ApiModelProperty(value = "是否可部署（0是，1否）", example = "0")
    @TableField("deployable")
    private String deployable;
    @ApiModelProperty(value = "是否可发布/上线（0是，1否）", example = "0")
    @TableField("publishable")
    private String publishable;

    @ApiModelProperty(value = "模型描述", example = "通用能力优异，适合作为基座模型进行精调，更好地处理特定场景问题，同时具备极佳的推理性能")
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


    @ApiModelProperty(value = "是否可用 ", example = "0：可用，1：不可用")
    @TableField("status")
    private String status;

    /**
     * 模型ID
     */
    @ApiModelProperty(value = "模型ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modelId;

    @ApiModelProperty(value = "出入参")
    @TableField("access_parameters")
    private String accessParameters;

    @ApiModelProperty(value = "部署状态")
    @TableField("deploy_status")
    private String deployStatus;

    @ApiModelProperty(value = "推理模型名称")
    @TableField("reasoning_model")
    private String reasoningModel;
}