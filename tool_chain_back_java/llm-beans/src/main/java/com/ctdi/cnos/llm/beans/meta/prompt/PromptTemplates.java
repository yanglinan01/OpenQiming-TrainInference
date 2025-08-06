package com.ctdi.cnos.llm.beans.meta.prompt;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提示词模板表(promptTemplates)实体类
 *
 * @author wangyb
 * @since 2024-04-02 15:09:11
 */

@Data
@ApiModel("PromptTemplates实体类")
@TableName("meta.mm_prompt_templates")
public class PromptTemplates {
    private static final long serialVersionUID = 957826957439763341L;

    @ApiModelProperty(value = "id", example = "17767669433049344")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal id;

    @ApiModelProperty(value = "模板名称", example = "Basic Prompt Framework")
    private String templateName;

    @ApiModelProperty(value = "模板内容", example = "Basic Prompt Framework")
    private String templateText;

    @ApiModelProperty(value = "创建人id", example = "-1", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal creatorId;

    @ApiModelProperty(value = "创建时间", example = "2024-04-01 08:00:00", hidden = true)
    private Date createDate;

    @ApiModelProperty(value = "修改人id", example = "-1", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal modifierId;

    @ApiModelProperty(value = "修改日期", example = "2024-04-01 08:00:00", hidden = true)
    private Date modifyDate;

    @ApiModelProperty(value = "描述", example = "Basic Prompt Framework")
    private String description;


}
