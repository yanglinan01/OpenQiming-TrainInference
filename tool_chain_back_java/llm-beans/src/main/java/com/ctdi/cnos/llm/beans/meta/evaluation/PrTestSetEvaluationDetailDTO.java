package com.ctdi.cnos.llm.beans.meta.evaluation;

import com.ctdi.cnos.llm.base.object.Groups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 问答对测试数据集评估详情 Dto对象。
 *
 * @author laiqi
 * @since 2024/09/04
 */
@ApiModel(description = "PrTestSetEvaluationDetailDto对象")
@Data
public class PrTestSetEvaluationDetailDTO {

    /**
     * 主键
     */
	@ApiModelProperty(value = "主键", required = true)
	@NotNull(message = "数据验证失败，主键不能为空！", groups = {Groups.UPDATE.class})
	private Long id;
	
    /**
     * 测试数据集评估ID
     */
    @ApiModelProperty(value = "测试数据集评估ID")
    @NotNull(message = "数据验证失败，主键不能为空！", groups = {Groups.PAGE.class, Groups.QUERY.class, Groups.ADD.class})
	private Long testSetEvaluationId;

    /**
     * 问答对详情ID(mm_prompt_response_detail.id)
     */
    @ApiModelProperty(value = "问答对详情ID(mm_prompt_response_detail.id)")
    @NotNull(message = "数据验证失败，问答对详情ID不能为空！", groups = {Groups.ADD.class})
    private Long promptResponseDetailId;
	
    /**
     * 推理答案
     */
    @ApiModelProperty(value = "推理答案")
	private String reasoningResponse;
	
    /**
     * 推理时间
     */
    @ApiModelProperty(value = "推理时间")
	private Date reasoningDate;

}