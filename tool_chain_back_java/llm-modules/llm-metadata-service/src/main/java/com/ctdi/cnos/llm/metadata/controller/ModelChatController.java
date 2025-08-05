package com.ctdi.cnos.llm.metadata.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.beans.meta.constant.ApplicationType;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationBodyDTO;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationContent;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationContentType;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationResponse;
import com.ctdi.cnos.llm.beans.meta.model.ProxyCustomerModelChatDTO;
import com.ctdi.cnos.llm.beans.reason.req.LlmTimeModelPredictionDTO;
import com.ctdi.cnos.llm.beans.reason.vo.LlmTimeModelPredictionVo;
import com.ctdi.cnos.llm.metadata.extra.conversation.ConversationProcessorFactory;
import com.ctdi.cnos.llm.metadata.service.ModelChatService;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 。
 *
 * @author laiqi
 * @since 2024/9/4
 */
@Api(tags = {"模型体验"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/model")
@Slf4j
public class ModelChatController {

    private final ModelChatService modelChatService;

    /**
     * 时序大模型体验预测。
     *
     * @param request 时序大模型体验请求对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "时序大模型体验预测", notes = "时序大模型体验预测")
    @PostMapping(value = "/llmTimeModelPrediction")
    public LlmTimeModelPredictionVo llmTimeModelPrediction(@Validated @RequestBody LlmTimeModelPredictionDTO request) {
        return modelChatService.llmTimeModelPrediction(request);
    }

    /**
     * 代理自建模型体验对话。
     *
     * @param request 请求对象。
     * @return 原接口应答对象。
     */
    @AuthIgnore
    @ApiOperation(value = "代理自建模型体验对话", notes = "代理自建模型体验对话")
    @PostMapping(value = "/proxyCustomerModelChat")
    public OperateResult<String> proxyCustomerModelChat(@Validated @RequestBody ProxyCustomerModelChatDTO request) {
        try {
            ConversationBodyDTO conversationBody = new ConversationBodyDTO();
            conversationBody.setSessionId(null);
            conversationBody.setModel(request.getTaskId());
            conversationBody.setApplicationType(ApplicationType.SELF_BUILT_MODEL);
            JSONObject params = new JSONObject();
            params.put("top_p", request.getTop_p());
            params.put("temperature", request.getTemperature());
            params.put("stream", false);
            params.put("presence_penalty", request.getPresence_penalty());
            params.put("frequency_penalty", request.getFrequency_penalty());
            params.put("max_tokens", request.getMax_tokens());
            String content = request.getContent();
            List<?> messages = request.getMessages();
            if (StrUtil.isNotEmpty(content)) {
                conversationBody.setContents(CollectionUtil.newLinkedList(new ConversationContent().setContent(content)));
            }
            else if (CollUtil.isNotEmpty(messages)) {
                JSONObject ext = new JSONObject();
                ext.put("messages", messages);
                conversationBody.setContents(CollectionUtil.newLinkedList(new ConversationContent().setContentType(ConversationContentType.PROXY).setExt(ext)));
            }
            else {
                return OperateResult.error("缺少对话信息");
            }
            conversationBody.setParams(params);
            OperateResult<ConversationResponse> operateResult = ConversationProcessorFactory.callConversationProcessor(conversationBody);
            return operateResult.isSuccess() ? OperateResult.success(operateResult.getData().getOriginal()) : OperateResult.error(operateResult.getMessage());
        } catch (Exception e) {
            return OperateResult.error(e.getMessage());
        }
    }
}