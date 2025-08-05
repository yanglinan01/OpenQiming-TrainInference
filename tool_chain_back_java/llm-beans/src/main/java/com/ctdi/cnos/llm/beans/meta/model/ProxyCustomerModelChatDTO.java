package com.ctdi.cnos.llm.beans.meta.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 代理自建模型体验请求对象。
 *
 * @author laiqi
 * @since 2024/9/6
 */
@ApiModel("代理自建模型体验请求对象")
@Setter
@Getter
public class ProxyCustomerModelChatDTO implements Serializable {

    @ApiModelProperty("模型任务ID")
    @NotNull(message = "数据验证失败，模型任务ID不能为空！")
    private String taskId;

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
     * 频率惩罚介于-2.0和2.0之间，它影响模型如何根据文本中词汇的现有频率惩罚新词汇。正值将通过惩罚已经频繁使用的词来降低模型一行中重复用词的可能性。
     */
    @ApiModelProperty(value = "频率惩罚介于-2.0和2.0之间，它影响模型如何根据文本中词汇的现有频率惩罚新词汇。正值将通过惩罚已经频繁使用的词来降低模型一行中重复用词的可能性。取值范围[-2.0, 2.0]，默认值0.0。", example = "0.0")
    private Float frequency_penalty;

    /**
     * 存在惩罚介于-2.0和2.0之间，它影响模型如何根据到目前为止是否出现在文本中来惩罚新token。正值将通过惩罚已经使用的词，增加模型谈论新主题的可能性。
     */
    @ApiModelProperty(value = "存在惩罚介于-2.0和2.0之间，它影响模型如何根据到目前为止是否出现在文本中来惩罚新token。取值范围[-2.0, 2.0]，默认值0.0。", example = "0.0")
    private Float presence_penalty;

    /**
     * 响应中的最大令牌数量
     */
    @ApiModelProperty(value = "允许推理生成的最大token个数。", example = "512")
    private Integer max_tokens = 512;

    /**
     * 推理请求文本
     */
    @ApiModelProperty(value = "content")
    private String content;

    /**
     * 透传messages
     */
    @ApiModelProperty(value = "content")
    private List<?> messages;

}