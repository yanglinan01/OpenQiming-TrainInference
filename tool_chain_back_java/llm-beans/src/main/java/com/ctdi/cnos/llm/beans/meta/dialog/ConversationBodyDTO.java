package com.ctdi.cnos.llm.beans.meta.dialog;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.exception.MyRuntimeException;
import com.ctdi.cnos.llm.util.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 会话请求对象。
 *
 * @author laiqi
 * @since 2024/9/14
 */
@ApiModel(description = "会话请求对象")
@Data
public class ConversationBodyDTO {

    /**
     * 会话唯一标识符，用来关联一系列相关请求。
     */
    @ApiModelProperty(value = "会话唯一标识符，用来关联一系列相关请求。", required = true)
    @NotBlank(message = "数据验证失败，会话唯一标识符不能为空！")
    private String sessionId;

    /**
     * 请求的唯一标识符，便于追踪和调试
     */
    @ApiModelProperty(value = "请求的唯一标识符")
    private String requestId;

    /**
     * 模型标识符，用于指定处理请求的具体模型版本或类型。
     */
    @ApiModelProperty(value = "模型标识符", required = true)
    @NotBlank(message = "数据验证失败，模型标识符不能为空！")
    private String model;

    /**
     * 应用类型，用于指定处理请求的具体模型类型。参考字典：APPLICATION_TYPE
     */
    @NotNull(message = "数据验证失败，应用类型不能为空！")
    private Integer applicationType;

    /**
     * 参数对象，包含额外的配置选项。
     */
    @ApiModelProperty(value = "参数对象，包含额外的配置选项")
    private JSONObject params;

    /**
     * 内容列表，包含本次会话的所有消息。
     */
    @ApiModelProperty(value = "内容列表，包含本次会话的所有消息", required = true)
    @Valid
    @NotNull(message = "数据验证失败，内容列表不能为空！")
    @Size(min = 1, message = "数据验证失败，内容列表至少包含一个元素！")
    private List<ConversationContent> contents;

    /**
     * 是否mock测试
     */
    @ApiModelProperty(value = "是否mock测试")
    private Boolean mock;

    /**
     * 模型标识符，用于指定处理请求的具体模型版本或类型。
     */
    @ApiModelProperty(value = "推理模型名称")
    private String reasoningModel;

    /**
     * 提取问题
     *
     * @return
     */
    public String extractQuestion() {
        // 获取集合对象中role是user最后一条记录，提取content
        return contents.stream()
                .filter(content -> SystemConstant.CONVERSATION_CONTENT_DEFAULT_ROLE_VALUE.equals(content.getRole()))
                .reduce((first, second) -> second)
                .map(ConversationContent::getContent)
                .orElse("");
    }

    /**
     * 构建代理模型请求体
     *
     * @return
     */
    public JSONObject buildCustomerModelRequestBody() {
        JSONObject body = new JSONObject();
        String model=this.model;
        if(StringUtils.isNotEmpty(this.reasoningModel)){
            model=this.reasoningModel;
        }
        body.put("model", model);
        int max_tokens = 1000;
        if (params.containsKey("max_tokens")) {
            max_tokens = params.getInteger("max_tokens");
        }
        body.put("max_tokens", max_tokens);
        if (params.containsKey("presence_penalty")) {
            body.put("presence_penalty", params.getFloat("presence_penalty"));
        }
        if (params.containsKey("frequency_penalty")) {
            body.put("frequency_penalty", params.getFloat("frequency_penalty"));
        }
        if (params.containsKey("seed")) {
            body.put("seed", params.getFloat("seed"));
        }
        // 温度
        float temperature = 0.8F;
        if (params.containsKey("temperature")) {
            temperature = params.getFloat("temperature");
        }
        body.put("temperature", temperature);
        if (params.containsKey("top_p")) {
            body.put("top_p", params.getFloat("top_p"));
        }
        // 特别注意：时序一定得是true
        if (params.containsKey("stream")) {
            body.put("stream", params.getBoolean("stream"));
        }

        List<?> messages = getMessages();
        body.put("messages", messages);
        return body;
    }

    public List<?> getMessages() {
        if (CollUtil.isEmpty(contents)) {
            throw new MyRuntimeException("对话内容不能为空！");
        }
        // 单个内容的处理方式
        if (CollUtil.size(contents) == 1) {
            ConversationContent conversationContent = contents.get(0);
            ConversationContentType contentType = conversationContent.getContentType();
            if (ConversationContentType.TEXT == contentType) {
                String content = conversationContent.getContent();
                JSONObject userMessage = new JSONObject();
                userMessage.put("role", "user");
                userMessage.put("content", content);
                JSONArray messages = new JSONArray();
                messages.add(ApplicationConstant.SYSTEM_MESSAGE);
                messages.add(userMessage);
                return messages;
            } else if (ConversationContentType.PROXY == contentType) {
                return conversationContent.getExt().getObject("messages", List.class);
            }
        }
        // 多个内容的处理方式
        else {
            JSONArray messages = new JSONArray();
            // 判断contents的元素中的role是否存在system，不存在时添加一条默认的
            boolean hasSystemRole = contents.stream().anyMatch(content -> SystemConstant.CONVERSATION_CONTENT_SYSTEM_ROLE_VALUE.equalsIgnoreCase(content.getRole()));
            if (!hasSystemRole) {
                // 追加系统消息
                messages.add(ApplicationConstant.SYSTEM_MESSAGE);
            }
            for (ConversationContent content : contents) {
                JSONObject message = new JSONObject();
                message.put("role", content.getRole());
                message.put("content", content.getContent());
                messages.add(message);
            }
            return messages;
        }
        return null;
    }

    /**
     * 是否开启流式输出
     * @return
     */
    public Boolean extractedStream() {
        if (params.containsKey("stream")) {
            return params.getBoolean("stream");
        }
        return false;
    }
}