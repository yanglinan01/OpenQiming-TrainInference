/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.prompt;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctdi.cnos.llm.base.object.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Prompt 实体
 *
 * @author huangjinhua
 * @since 2024/4/2
 */

@Getter
@Setter
@TableName("meta.mm_prompts")
@ApiModel(value = "Prompts对象", description = "提示词存储表")
public class Prompt extends BaseModel {

    @ApiModelProperty(value = "主键", required = true)
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "提示词内容", required = true)
    @TableField("prompt_text")
    private String promptText;

    @ApiModelProperty(value = "模板ID,mm_prompt_templates.id,-1时为未绑定模版")
    @TableField("model_id")
    private Long modelId;

    @ApiModelProperty(value = "名称", required = true)
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "变量标识符,详见字典PROMPT_IDENTIFIER")
    @TableField("identifier")
    private String identifier;

    @ApiModelProperty(value = "类别，详见字典PROMPT_TYPE。-1为自定义缺省类型", required = true)
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "归属，详见字典PROMPT_BELONG", required = true)
    @TableField("belong")
    private String belong;

    @ApiModelProperty(value = "变量名称")
    @TableField("variable")
    private String variable;

    @ApiModelProperty(value = "接口信息")
    @TableField("int_url")
    private String intUrl;

    @ApiModelProperty(value = "区域编码")
    @TableField("region_code")
    private String regionCode;
}