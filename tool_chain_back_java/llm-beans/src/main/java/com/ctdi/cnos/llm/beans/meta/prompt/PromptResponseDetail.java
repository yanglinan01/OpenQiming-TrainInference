package com.ctdi.cnos.llm.beans.meta.prompt;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yuyong
 * @date 2024/8/15 11:00
 */
@Data
@ApiModel("问答对详情实体类")
@TableName("meta.mm_prompt_response_detail")
public class PromptResponseDetail {

    /**
     * id
     */
    @TableId("id")
    @ApiModelProperty(value = "id", example = "example")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal id;
    /**
     * 数据集文件id;数据集_20240514_104036
     */
    @ApiModelProperty(value = "数据集文件id;数据集_20240514_104036", example = "example")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal dataSetFileId;

    /**
     * 问题id
     */
    @ApiModelProperty(value = "文件类型", example = "example")
    private int questionId;

    /**
     * 对话轮次
     */
    @ApiModelProperty(value = "文件类型", example = "example")
    private int rank;

    /**
     * 问题
     */
    @ApiModelProperty(value = "文件类型", example = "example")
    private String prompt;

    /**
     * 回答
     */
    @ApiModelProperty(value = "文件类型", example = "example")
    private String response;

    /**
     * 状态
     */
    @ApiModelProperty(value = "文件类型", example = "example")
    private String status;

    /**
     * 创建人id;1
     */
    @ApiModelProperty(value = "创建人id;1", example = "example")
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
}
