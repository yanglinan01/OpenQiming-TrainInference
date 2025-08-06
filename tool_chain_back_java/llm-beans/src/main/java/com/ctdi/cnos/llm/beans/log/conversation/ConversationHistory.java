/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.log.conversation;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 历史记录 实体
 *
 * @author huangjinhua
 * @since 2024/5/10
 */
@Data
@ApiModel("历史记录 实体类")
public class ConversationHistory implements Serializable {
    private static final long serialVersionUID = -12685702896326562L;
    @ApiModelProperty(value = "ID", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonIgnore
    private Long id;
    @ApiModelProperty(value = "用户ID")
    @JsonProperty("user_id")
    private String userId;
    @ApiModelProperty(value = "会话ID")
    @JsonProperty("session_id")
    private String sessionId;
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
    @ApiModelProperty(value = "记录时间")
    @JsonProperty("time")
    private Date time;

    @JsonIgnore
    private String userStateText;
}