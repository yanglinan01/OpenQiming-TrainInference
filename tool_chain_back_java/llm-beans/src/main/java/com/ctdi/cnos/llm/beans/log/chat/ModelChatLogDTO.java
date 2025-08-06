package com.ctdi.cnos.llm.beans.log.chat;

import cn.hutool.core.util.BooleanUtil;
import com.ctdi.cnos.llm.base.object.Groups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 模型体验对话日志 Dto对象。
 *
 * @author laiqi
 * @since 2024/07/16
 */
@ApiModel(description = "ModelChatLogDto对象")
@Data
public class ModelChatLogDTO {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "数据验证失败，id不能为空！", groups = {Groups.UPDATE.class})
    private Long id;

    /**
     * 模型对话类型。对应字典：APPLICATION_TYPE
     */
    @ApiModelProperty(value = "模型对话类型。对应字典：APPLICATION_TYPE")
    private Integer modelChatType;

    /**
     * 模型体验的ID
     */
    @ApiModelProperty(value = "模型体验的ID")
    private Long modelChatId;

    /**
     * 会话ID
     */
    @ApiModelProperty(value = "会话ID")
    private String sessionId;

    /**
     * 发送人
     */
    @ApiModelProperty(value = "发送人")
    private Long sendUserId;

    /**
     * 发送消息
     */
    @ApiModelProperty(value = "发送消息")
    private String sendMessage;

    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    private Date sendTime;

    /**
     * 响应消息
     */
    @ApiModelProperty(value = "响应消息")
    private String responseMessage;

    /**
     * 响应时间
     */
    @ApiModelProperty(value = "响应时间")
    private Date responseTime;

    /**
     * 文本推理还是流式推理
     */
    @ApiModelProperty(value = "文本推理还是流式推理")
    private Boolean stream;

    /**
     * 输入提示词Token数
     */
    @ApiModelProperty(value = "输入提示词Token数")
    private Integer promptTokens;

    /**
     * 模型生成Token数
     */
    @ApiModelProperty(value = "模型生成Token数")
    private Integer completionTokens;

    /**
     * 整个请求和响应中使用的总Token数量
     */
    @ApiModelProperty(value = "整个请求和响应中使用的总Token数量")
    private Integer totalTokens;

    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
        this.sendTime = new Date();
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
        this.responseTime = new Date();
    }

    /**
     * 设置Tokens
     * @param promptTokens 输入提示词Token数
     * @param completionTokens 模型生成Token数
     */
    public void setTokens(Integer promptTokens, Integer completionTokens) {
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.totalTokens = promptTokens + completionTokens;
    }

    @ApiModelProperty(value = "响应消息构造器。主要处理流式拼接", hidden = true)
    private StringBuilder responseMessageBuilder = new StringBuilder();

    /**
     * 追加响应消息
     *
     * @param message 消息
     */
    public void appendResponseMessage(String message) {
        appendResponseMessage(message, null);
    }

    /**
     * 追加响应消息
     *
     * @param message            消息
     * @param setResponseMessage 设置到响应消息
     */
    public void appendResponseMessage(String message, Boolean setResponseMessage) {
        this.responseMessageBuilder.append(message);
        if (BooleanUtil.isTrue(setResponseMessage)) {
            this.setResponseMessage(responseMessageBuilder.toString());
            // 清空StringBuilder的内容
            this.responseMessageBuilder.setLength(0);
        }
    }
}