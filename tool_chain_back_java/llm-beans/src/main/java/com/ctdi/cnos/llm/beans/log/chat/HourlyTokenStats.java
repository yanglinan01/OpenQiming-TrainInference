package com.ctdi.cnos.llm.beans.log.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * 按小时统计的模型Token数量。
 *
 * @author laiqi
 * @since 2024/11/26
 */
@FieldNameConstants
@Data
public class HourlyTokenStats {

    @ApiModelProperty(value = "小时。")
    private String hour;

    /**
     * 输入提示词token数。
     */
    @ApiModelProperty(value = "输入提示词token数。")
    private Integer promptTokensSum;

    /**
     * 模型生成token数。
     */
    @ApiModelProperty(value = "模型生成token数。")
    private Integer completionTokensSum;

    /**
     * 总token数。
     */
    @ApiModelProperty(value = "总token数。")
    private Integer totalTokensSum;
}