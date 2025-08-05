package com.ctdi.cnos.llm.beans.reason.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 模型体验HTTP请求对象。
 *
 * @author laiqi
 * @since 2024/6/19
 */
@Setter
@Getter
@ApiModel("大模型体验请求对象")
public class ModelStartChatRequest {

    /**
     * 停止响应的令牌 ID 列表(必须有值)
     */
    @ApiModelProperty(value = "停止响应的令牌 ID 列表", example = "[151645, 151644, 151643]", hidden = true)
    private int[] stop_token_ids = new int[]{151645, 151644, 151643};

    /**
     * 如果为负数，则不限制采样(可以为空值)
     */
    @ApiModelProperty(value = "如果为负数，则不限制采样", example = "-1", hidden = true)
    private Integer top_k = -1;

    /**
     * 响应中的最大令牌数量(可以为空值)
     */
    @ApiModelProperty(value = "响应中的最大令牌数量", example = "512", hidden = true)
    private Integer max_tokens = 1000;

    /**
     * 会话识别码，用于区分不同会话
     */
    @ApiModelProperty(value = "会话识别码，用于区分不同会话，UUID", example = "dbf4af03-cdef-45e4-9567-30bb96e90499")
    private String session_id;

    /**
     * 用户问题
     */
    @ApiModelProperty(value = "用户问题", required = true, example = "先有蛋还是先有鸡")
    private String question;

    /**
     * “true”：打开流式输出
     * “false”：关闭流式输出
     */
    @ApiModelProperty(value = "流式输出，true表示打开流失输出;false表示关闭流失输出。默认是true", example = "true", dataType = "boolean")
    private Boolean stream = true;

    /**
     * 控制采样多样性
     */
    @ApiModelProperty(value = "控制采样多样性", example = "0.8")
    private Float top_p = 0.8F;

    /**
     * 控制输出的随机性
     */
    @ApiModelProperty(value = "控制输出的随机性", example = "0.8")
    private Float temperature = 0.8F;
}