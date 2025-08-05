package com.ctdi.cnos.llm.beans.log.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 指定模型ID在所有时间范围内的调用总Token、请求Token、响应Token和总调用数。
 *
 * @author laiqi
 * @since 2024/11/27
 */
@Data
public class ModelCallSummary {
    /**
     * 输入提示词token总数
     */
    @ApiModelProperty(value = "输入提示词token总数")
    private int totalPromptTokens;
    /**
     * 模型生成token总数
     */
    @ApiModelProperty(value = "模型生成token总数")
    private int totalCompletionTokens;
    /**
     * token总数
     */
    @ApiModelProperty(value = "token总数")
    private int totalTokens;
    /**
     * 总调用数
     */
    @ApiModelProperty(value = "总调用数")
    private int totalCalls;
}