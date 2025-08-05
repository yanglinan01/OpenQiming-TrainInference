package com.ctdi.cnos.llm.beans.reason.req;

import cn.hutool.core.util.StrUtil;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 自建模型体验HTTP请求对象。
 *
 * @author laiqi
 * @since 2024/7/23
 */
@Setter
@Getter
@ApiModel("自建模型体验HTTP请求对象")
public class CustomerModelStartChatRequest {
    /**
     * 模型名，当前不校验该字段。(这里使用模型推理部署Id)
     * 为了兼容dcoss。这里也充当接口方的registerId。
     *
     * @see DeployTask#getRegisterId()
     * @see DeployTask#getId()
     */
    @ApiModelProperty(value = "模型名(这里使用模型推理部署Id)", required = true)
    @NotBlank(message = "数据验证失败，模型名不能为空！")
    private String model;

    /**
     * 消息体对象
     */
    @Valid
    @ApiModelProperty(value = "消息体对象", required = true)
    @NotNull(message = "数据验证失败，消息体对象不能为空！")
    private List<Message> messages;

    /**
     * 响应中的最大令牌数量
     */
    @ApiModelProperty(value = "允许推理生成的最大token个数。", example = "512", hidden = true)
    private Integer max_tokens = 1000;


    /**
     * 存在惩罚介于-2.0和2.0之间，它影响模型如何根据到目前为止是否出现在文本中来惩罚新token。正值将通过惩罚已经使用的词，增加模型谈论新主题的可能性。
     */
    @ApiModelProperty(value = "存在惩罚介于-2.0和2.0之间，它影响模型如何根据到目前为止是否出现在文本中来惩罚新token。取值范围[-2.0, 2.0]，默认值0.0。", example = "0.0", hidden = true)
    private Float presence_penalty;

    /**
     * 频率惩罚介于-2.0和2.0之间，它影响模型如何根据文本中词汇的现有频率惩罚新词汇。正值将通过惩罚已经频繁使用的词来降低模型一行中重复用词的可能性。
     */
    @ApiModelProperty(value = "频率惩罚介于-2.0和2.0之间，它影响模型如何根据文本中词汇的现有频率惩罚新词汇。正值将通过惩罚已经频繁使用的词来降低模型一行中重复用词的可能性。取值范围[-2.0, 2.0]，默认值0.0。", example = "0.0", hidden = true)
    private Float frequency_penalty;


    /**
     * 用于指定推理过程的随机种子，相同的seed值可以确保推理结果的可重现性，不同的seed值会提升推理结果的随机性。
     * 取值范围(0, 18446744073709551615]，不传递该参数，系统会产生一个随机seed值。
     */
    @ApiModelProperty(value = "用于指定推理过程的随机种子，相同的seed值可以确保推理结果的可重现性，不同的seed值会提升推理结果的随机性。取值范围(0, 18446744073709551615]，不传递该参数，系统会产生一个随机seed值。", example = "10000", hidden = true)
    private Long seed;

    /**
     * 控制输出的随机性。
     * 控制生成的随机性，较高的值会产生更多样化的输出。
     * 取值越大，结果的随机性越大。推荐使用大于或等于0.001的值，小于0.001可能会导致文本质量不佳。
     * 较高的值会产生更多样化的输出。取值范围(0.0, 2.0]，默认值1.0。
     */
    @ApiModelProperty(value = "控制输出的随机性。取值范围(0.0, 2.0]，默认值1.0。", example = "0.8")
    private Float temperature = 0.8F;

    /**
     * 控制采样多样性。
     * 控制模型生成过程中考虑的词汇范围，使用累计概率选择候选词，直到累计概率超过给定的阈值。该参数也可以控制生成结果的多样性，它基于累积概率选择候选词，直到累计概率超过给定的阈值为止。
     * 取值范围(0.0, 1.0]，默认值1.0。
     */
    @ApiModelProperty(value = "控制采样多样性。取值范围(0.0, 1.0]，默认值1.0。", example = "0.8")
    private Float top_p = 0.8F;

    /**
     * “true”：打开流式输出
     * “false”：关闭流式输出
     */
    @ApiModelProperty(value = "流式输出，true表示打开流失输出;false表示关闭流失输出。默认是true", example = "true", dataType = "boolean")
    private boolean stream = false;

    /**
     * 单个消息体对象。
     */
    @Accessors(chain = true)
    @Data
    public static class Message {
        /**
         * 推理请求消息角色。
         */
        @ApiModelProperty(value = "role", required = true)
        @NotBlank(message = "数据验证失败，推理请求消息角色不能为空！")
        private String role = "user";

        /**
         * 推理请求文本
         */
        @ApiModelProperty(value = "content", required = true)
        @NotBlank(message = "数据验证失败，推理请求文本不能为空！")
        private String content;
    }

    /**
     * 创建系统角色消息(用于携带，需要把该消息放到集合第一个)
     *
     * @param content
     * @return
     */
    public static Message createSystemMessage(String content) {
        content = StrUtil.nullToDefault(content, "You are a helpful assistant. 你是电信的启明大模型，是一个专业的AI助手，你擅长回答用户的QA问题，并且你的回复无害、详细且准确。");
        Message message = new Message();
        message.setRole("system");
        message.setContent(content);
        return message;
    }
}