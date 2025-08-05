package com.ctdi.cnos.llm.beans.log.chat;

import com.ctdi.cnos.llm.base.object.BaseVO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 模型体验对话日志 Vo对象。
 *
 * @author laiqi
 * @since 2024/07/16
 */
@ApiModel(description = "ModelChatLogVo对象")
@Data
public class ModelChatLogVO extends BaseVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 模型对话类型
     */
    @ApiModelProperty(value = "模型对话类型")
    private Integer modelChatType;

    /**
     * 模型体验的ID
     */
    @ApiModelProperty(value = "模型体验的ID")
    @JsonSerialize(using = ToStringSerializer.class)
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
    @JsonSerialize(using = ToStringSerializer.class)
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


    /**
     * 模型ID列表
     */
    @ApiModelProperty(value = "模型ID列表")
    private List<Long> modelIdList;

    /**
     * 延时，天
     */
    @ApiModelProperty(value = "延时，天")
    private Integer offsetDay;

}