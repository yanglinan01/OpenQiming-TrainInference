package com.ctdi.cnos.llm.beans.meta.evaluation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

/**
 * 问答对测试数据集评估详情 实体对象。
 *
 * @author laiqi
 * @since 2024/09/04
 */
@FieldNameConstants
@Getter
@Setter
@TableName("meta.mm_pr_test_set_evaluation_detail")
@ApiModel(value = "PrTestSetEvaluationDetail对象", description = "问答对测试数据集评估详情")
public class PrTestSetEvaluationDetail {

	@ApiModelProperty(value = "主键", required = true)
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "测试数据集评估ID")
    @TableField("test_set_evaluation_id")
    private Long testSetEvaluationId;

    @ApiModelProperty(value = "问答对详情ID(mm_prompt_response_detail.id)")
    @TableField("prompt_response_detail_id")
    private Long promptResponseDetailId;

    @ApiModelProperty(value = "推理答案")
    @TableField("reasoning_response")
    private String reasoningResponse;

    @ApiModelProperty(value = "推理时间")
    @TableField("reasoning_date")
    private Date reasoningDate;

    @ApiModelProperty(value = "推理答案2")
    @TableField("reasoning_two_response")
    private String reasoningTwoResponse;

    @ApiModelProperty(value = "推理时间2")
    @TableField("reasoning_two_date")
    private Date reasoningTwoDate;

}