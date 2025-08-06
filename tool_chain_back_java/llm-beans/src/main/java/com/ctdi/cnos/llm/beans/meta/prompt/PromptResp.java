package com.ctdi.cnos.llm.beans.meta.prompt;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 问答详情(PromptResp)实体类
 * @author wangyb
 * @since 2024-05-15 14:00:19
 */

@Data
@ApiModel("PromptResp实体类")
@TableName("meta.mm_prompt_resp")
public class PromptResp {
    private static final long serialVersionUID = -69311389173804423L;
    /**
     * id;id
     */	 
	@ApiModelProperty(value = "id", example = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal id;
    /**
     * 数据集id
     */	 
	@ApiModelProperty(value = "数据集id", example = "103113891738042")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal dataSetId;
    /**
     * 内容
     */	 
	@ApiModelProperty(value = "内容", example = "xxxx")
    private String context;

    /**
     * 创建人id;1
     */	 
	@ApiModelProperty(value = "创建人id;", example = "example")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal creatorId;
    /**
     * 创建时间
     */	 
	@ApiModelProperty(value = "创建时间", example = "example")
    private Date createDate;
    /**
     * 修改人id
     */	 
	@ApiModelProperty(value = "修改人id", example = "example")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal modifierId;
    /**
     * 修改时间
     */	 
	@ApiModelProperty(value = "修改时间", example = "example")
    private Date modifyDate;
    /**
     * 是否有效
     */	 
	@ApiModelProperty(value = "是否有效", example = "example")
    private String isValid;
    /**
     * 是否删除
     */	 
	@ApiModelProperty(value = "是否删除", example = "example")
    private String isDelete;


}

