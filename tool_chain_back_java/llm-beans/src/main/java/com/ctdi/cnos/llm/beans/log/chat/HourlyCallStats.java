package com.ctdi.cnos.llm.beans.log.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * 按小时统计的模型调用数量。
 *
 * @author laiqi
 * @since 2024/11/27
 */
@FieldNameConstants
@Data
public class HourlyCallStats {

    @ApiModelProperty(value = "小时。")
    private String hour;

    /**
     * 模型调用数。
     */
    @ApiModelProperty(value = "模型调用数。")
    private Integer totalCalls;
}