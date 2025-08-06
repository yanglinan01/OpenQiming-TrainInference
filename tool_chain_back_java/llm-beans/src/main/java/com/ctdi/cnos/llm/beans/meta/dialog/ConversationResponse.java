package com.ctdi.cnos.llm.beans.meta.dialog;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会话响应对象。
 * 用于封装 HTTP 响应的内容，包括原始内容和提取的内容。
 *
 * @author laiqi
 * @since 2024/9/14
 */
@ApiModel(description = "会话响应对象")
@Data
public class ConversationResponse {

    /**
     * 提取的内容。
     */
    @ApiModelProperty(value = "提取的内容")
    private String extractedContent;

    /**
     * 原始内容。
     */
    @ApiModelProperty(value = "原始内容")
    private String original;
}