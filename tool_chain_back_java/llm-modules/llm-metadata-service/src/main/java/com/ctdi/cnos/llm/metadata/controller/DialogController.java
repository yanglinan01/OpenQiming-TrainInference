package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.beans.meta.dialog.ConversationBodyDTO;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationResponse;
import com.ctdi.cnos.llm.metadata.extra.conversation.ConversationProcessorFactory;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对话控制器。
 * <p>
 * 该控制器类负责处理与对话相关的HTTP请求，并将请求转发给相应的服务层进行处理。
 * </p>
 *
 * @author laiqi
 * @since 2024/9/14
 */
@Api(tags = {"新模型体验对话"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/dialog")
public class DialogController {


    /**
     * 处理对话请求。
     * <p>
     * 该方法接收一个包含对话信息的请求体，并调用服务层的方法来处理对话逻辑。
     * 如果请求体验证通过，则返回处理结果；否则返回验证错误信息。
     * </p>
     *
     * @param conversationBody 包含对话信息的请求体
     * @return 操作结果，通常包含对话处理的结果信息
     */
    @ApiOperation(value = "HTTP对话(支持流式和非流式)", notes = "HTTP对话(支持流式和非流式)")
    @PostMapping(value = "/conversation")
    public OperateResult<ConversationResponse> conversation(@Validated @RequestBody ConversationBodyDTO conversationBody) {
        return ConversationProcessorFactory.callConversationProcessor(conversationBody);
    }
}