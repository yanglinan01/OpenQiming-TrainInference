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
 * 问答对测试数据集评估赞踩 实体对象。
 *
 * @author laiqi
 * @since 2024/09/04
 */
@FieldNameConstants
@Getter
@Setter
@TableName("meta.mm_pr_test_set_evaluation_score")
@ApiModel(value = "PrTestSetEvaluationScore对象", description = "问答对测试数据集评估赞踩")
public class PrTestSetEvaluationScore {

	@ApiModelProperty(value = "主键", required = true)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "问答对测试集详情ID")
    @TableField("evaluation_detail_id")
    private Long evaluationDetailId;

    @ApiModelProperty(value = "操作人ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "操作(1赞或者选择回答1；-1踩；2选择回答2)")
    @TableField("action")
    private Integer action;

    @ApiModelProperty(value = "操作时间")
    @TableField("action_date")
    private Date actionDate;
}