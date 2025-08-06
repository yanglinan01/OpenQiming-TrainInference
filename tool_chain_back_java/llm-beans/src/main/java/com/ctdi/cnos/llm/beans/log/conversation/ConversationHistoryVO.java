/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.log.conversation;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 历史记录 VO
 *
 * @author huangjinhua
 * @since 2024/5/10
 */
@Data
@ApiModel("历史记录 VO")
public class ConversationHistoryVO implements Serializable {
    private static final long serialVersionUID = -12685702896326562L;
    @ApiModelProperty(value = "用户问题")
    @JsonProperty("question")
    private String question;
    @ApiModelProperty(value = "大模型的答案")
    @JsonProperty("answer")
    private String answer;
    @ApiModelProperty(value = "用户意图分类")
    @JsonProperty("intent")
    private String intent;
    @ApiModelProperty(value = "本轮对话的关键信息")
    @JsonProperty(value = "user_state")
    private JSONObject userState;
    @ApiModelProperty(value = "本轮对话的模型状态")
    @JsonProperty("model_state")
    private String modelState;
    @ApiModelProperty(value = "改写后的问题")
    @JsonProperty("real_question")
    private String realQuestion;
    @ApiModelProperty(value = "对话ID")
    @JsonProperty("topic_id")
    private Integer topicId;
}