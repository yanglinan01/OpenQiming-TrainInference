package com.ctdi.cnos.llm.beans.meta.prompt;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 意图识别数据集详情(PromptCategoryDetail)实体类
 * @author wangyb
 * @since 2024-08-16 15:13:16
 */

@Data
@ApiModel("PromptCategoryDetail实体类")
@TableName("meta.mm_prompt_category_detail")
public class PromptCategoryDetail {
    private static final long serialVersionUID = 714053226593421190L;
    /**
     * 主键
     */
    @TableId
	@ApiModelProperty(value = "主键", example = "example")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal id;
    /**
     * 数据集文件ID
     */	 
	@ApiModelProperty(value = "数据集文件ID", example = "example")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal dataSetFileId;
    /**
     * 提问角色
     */	 
	@ApiModelProperty(value = "提问角色", example = "example")
    private String questionRole;
    /**
     * 问题内容
     */	 
	@ApiModelProperty(value = "问题内容", example = "example")
    private String prompt;
    /**
     * 分类
     */	 
	@ApiModelProperty(value = "分类", example = "example")
    private String category;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", example = "example")
    private String status;
    /**
     * 备用字段1
     */	 
	@ApiModelProperty(value = "备用字段1", example = "example")
    private String param1;
    /**
     * 备用字段2
     */	 
	@ApiModelProperty(value = "备用字段2", example = "example")
    private String param2;
    /**
     * 创建人
     */	 
	@ApiModelProperty(value = "创建人", example = "example")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal creatorId;
    /**
     * 创建时间
     */	 
	@ApiModelProperty(value = "创建时间", example = "example")
    private Date createDate;
    /**
     * 更新人
     */	 
	@ApiModelProperty(value = "更新人", example = "example")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal modifierId;
    /**
     * 更新时间
     */	 
	@ApiModelProperty(value = "更新时间", example = "example")
    private Date modifyDate;


}

