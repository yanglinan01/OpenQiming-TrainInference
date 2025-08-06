package com.ctdi.cnos.llm.beans.log.chat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctdi.cnos.llm.base.annotation.UserFilterColumn;
import com.ctdi.cnos.llm.base.object.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

/**
 * 模型体验对话日志 实体对象。
 *
 * @author laiqi
 * @since 2024/07/16
 */
@Getter
@Setter
@FieldNameConstants
@TableName("log.mm_model_chat_log")
@ApiModel(value = "ModelChatLog对象", description = "模型体验对话日志")
public class ModelChatLog extends BaseModel {

	@ApiModelProperty(value = "id", required = true)
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "模型对话类型")
    @TableField("model_chat_type")
    private Integer modelChatType;

    @ApiModelProperty(value = "模型体验的ID")
    @TableField("model_chat_id")
    private Long modelChatId;

    @ApiModelProperty(value = "会话ID")
    @TableField("session_id")
    private String sessionId;

    @UserFilterColumn
    @ApiModelProperty(value = "发送人")
    @TableField("send_user_id")
    private Long sendUserId;

    @ApiModelProperty(value = "发送消息")
    @TableField("send_message")
    private String sendMessage;

    @ApiModelProperty(value = "发送时间")
    @TableField("send_time")
    private Date sendTime;

    @ApiModelProperty(value = "响应消息")
    @TableField("response_message")
    private String responseMessage;

    @ApiModelProperty(value = "响应时间")
    @TableField("response_time")
    private Date responseTime;

    /**
     * 文本推理还是流式推理
     */
    @ApiModelProperty(value = "文本推理还是流式推理")
    @TableField("stream")
    private Boolean stream;

    /**
     * 输入提示词Token数
     */
    @ApiModelProperty(value = "输入提示词Token数")
    @TableField("prompt_tokens")
    private Integer promptTokens;

    /**
     * 模型生成Token数
     */
    @ApiModelProperty(value = "模型生成Token数")
    @TableField("completion_tokens")
    private Integer completionTokens;

    /**
     * 整个请求和响应中使用的总Token数量
     */
    @ApiModelProperty(value = "整个请求和响应中使用的总Token数量")
    @TableField("total_tokens")
    private Integer totalTokens;

    @ApiModelProperty(value = "起始时间")
    @TableField(exist = false)
    private Date startTime;

    @ApiModelProperty(value = "截至时间")
    @TableField(exist = false)
    private Date endTime;

}