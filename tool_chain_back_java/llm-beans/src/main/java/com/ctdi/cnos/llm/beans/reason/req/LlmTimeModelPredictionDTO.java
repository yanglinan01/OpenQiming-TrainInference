package com.ctdi.cnos.llm.beans.reason.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 时序大模型体验预测请求。
 *
 * @author laiqi
 * @since 2024/8/15
 */
@Setter
@Getter
@ApiModel("时序大模型体验预测请求")
public class LlmTimeModelPredictionDTO {

    @ApiModelProperty(value = "模型ID", required = true, example = "1807586796071366656")
    @NotNull(message = "数据验证失败，模型ID不能为空！")
    private Long modelId;

    @ApiModelProperty(value = "数据集ID", required = true, example = "1")
    @NotNull(message = "数据验证失败，数据集ID不能为空！")
    private Long dataSetId;

    @ApiModelProperty(value = "预测时长", required = true, example = "1")
    @NotNull(message = "数据验证失败，预测时长不能为空！")
    @Min(value = 1, message = "数据验证失败，预测时长不能小于1小时！")
    @Max(value = 21, message = "数据验证失败，预测时长不能大于21小时！")
    private Integer duration;

    @ApiModelProperty(value = "是否测试。生产不需要传！！", example = "false")
    private boolean test = false;
}