/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.prompt;

import com.ctdi.cnos.llm.base.object.BaseVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PromptVO
 *
 * @author huangjinhua
 * @since 2024/4/17
 */
@Data
@ApiModel("Prompt vo类")
public class PromptVO extends BaseVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 提示词内容
     */
    @ApiModelProperty(value = "提示词内容")
    private String promptText;

    /**
     * 模板ID,mm_prompt_templates.id,-1时为未绑定模版
     */
    @ApiModelProperty(value = "模板ID,mm_prompt_templates.id,-1时为未绑定模版")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modelId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 变量标识符,详见字典PROMPT_IDENTIFIER
     */
    @ApiModelProperty(value = "变量标识符,详见字典PROMPT_IDENTIFIER")
    private String identifier;

    /**
     * 类别，详见字典PROMPT_TYPE。-1为自定义缺省类型
     */
    @ApiModelProperty(value = "类别，详见字典PROMPT_TYPE。-1为自定义缺省类型")
    private String type;

    /**
     * 归属，详见字典PROMPT_BELONG
     */
    @ApiModelProperty(value = "归属，详见字典PROMPT_BELONG")
    private String belong;

    /**
     * 变量名称
     */
    @ApiModelProperty(value = "变量名称")
    private String variable;

    /**
     * 接口信息
     */
    @ApiModelProperty(value = "接口信息")
    private String intUrl;

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码")
    private String regionCode;
    @ApiModelProperty(value = "类别名称")
    private String typeName;
    @ApiModelProperty(value = "类别，以逗号(,)分隔")
    @JsonIgnore
    private String types;
    @ApiModelProperty(value = "权限sql")
    @JsonIgnore
    private String dataScopeSql;
}