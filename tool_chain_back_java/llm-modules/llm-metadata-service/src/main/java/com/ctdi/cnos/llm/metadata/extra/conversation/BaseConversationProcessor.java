package com.ctdi.cnos.llm.metadata.extra.conversation;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.base.constant.CacheConstant;
import com.ctdi.cnos.llm.beans.log.chat.ModelChatLogDTO;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationBodyDTO;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationResponse;
import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.exception.MyRuntimeException;
import com.ctdi.cnos.llm.feign.log.ModelChatLogServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.TokenCounterUtil;
import com.dtflys.forest.backend.ContentType;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对话处理器。
 *
 * @param <M> 缓存的模型对象。必须保持在第一个。
 * @author laiqi
 * @since 2024/9/14
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseConversationProcessor<M> {

    /**
     * 用户日志记录
     */
    private ModelChatLogServiceClientFeign modelChatLogServiceClientFeign;

    /**
     * 用户日志记录
     */
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 默认缓存12小时
     */
    private CtgCache ctgCache;

    /**
     * stream流式内容正则表达式
     */
    public static Pattern STREAM_PATTERN = Pattern.compile("\"content\"\\s*:\\s*\"([^\"]*)\"");

    @PostConstruct
    public void init() {
        modelChatLogServiceClientFeign = SpringUtil.getBean(ModelChatLogServiceClientFeign.class);
        taskExecutor = SpringUtil.getBean(ThreadPoolTaskExecutor.class);
        ctgCache = SpringUtil.getBean(CtgCache.class);
    }

    /**
     * 是否匹配处理器
     *
     * @param conversationBody 会话请求体
     * @return 匹配结果
     */
    protected abstract boolean match(ConversationBodyDTO conversationBody);

    /**
     * 校验参数
     *
     * @param conversationBody 会话请求体
     * @return 参数校验结果
     */
    protected abstract boolean validate(ConversationBodyDTO conversationBody);

    /**
     * 获取模型信息
     *
     * @param conversationBody 会话请求体
     * @return 模型信息
     */
    protected M getCacheModel(ConversationBodyDTO conversationBody) {
        String modelId = conversationBody.getModel();
        String cacheId = conversationBody.getApplicationType() + ":" + modelId;
        Object modelData = ctgCache.getCache(CacheConstant.CONVERSATION_CACHE_KEY, cacheId);
        if (modelData == null) {
            M model = buildModel(conversationBody);
            if (model != null) {
                ctgCache.set(CacheConstant.CONVERSATION_CACHE_KEY, cacheId, model, CacheConstant.CACHE_EXPIRE_1_DAY);
                return model;
            }
        }
        Type typeArgument = TypeUtil.getTypeArgument(getClass(), 0);
        if (modelData == null) {
            throw new MyRuntimeException("获取到的模型信息不存在！" + modelId);
        }
        if (modelData instanceof String) {
            log.info("查询缓存的类型是字符串，直接查库模型类型：{}，模型信息：{}， 模型信息的类型：{}", typeArgument, modelData, modelData.getClass());
            return buildModel(conversationBody);
        }
        return Convert.convert(typeArgument, modelData);
    }

    /**
     * 获取模型信息
     *
     * @param conversationBody 会话请求体
     * @return 模型信息
     */
    protected abstract M buildModel(ConversationBodyDTO conversationBody);

    /**
     * 转换为请求对象
     *
     * @param conversationBody 会话请求体
     * @param model            模型信息
     * @return 请求对象
     */
    protected abstract ForestRequest<?> convertForestRequest(ConversationBodyDTO conversationBody, M model);

    /**
     * 对话
     *
     * @param conversationBody 会话请求体
     * @return 操作结果
     */
    public OperateResult<ConversationResponse> process(ConversationBodyDTO conversationBody) {
        // 1、参数校验
        if (!validate(conversationBody)) {
            return OperateResult.error("参数校验失败！");
        }
        M model = getCacheModel(conversationBody);
        // 2、转换为请求对象
        ForestRequest<?> request = null;
        try {
            request = convertForestRequest(conversationBody, model);
        } catch (Exception e) {
            return OperateResult.error("转换为请求对象异常：" + e.getMessage());
        }
        // 3、组装日志对象
        ModelChatLogDTO chatLog = null;
        try {
            chatLog = assembleLog(conversationBody, request);
        } catch (Exception e) {
            return OperateResult.error("组装日志对象异常：" + e.getMessage());
        }
        // 4、执行请求
        log.info("开始执行模型推理请求：request：{},模型id：{}",request.getBody(), conversationBody.getModel());
        ForestResponse<?> forestResponse = request.executeAsResponse();
        log.info("模型推理请求结果：{}", forestResponse.readAsString());
        Throwable exception = forestResponse.getException();
        if (exception != null) {
            return OperateResult.error("执行请求异常：" + exception.getMessage());
        }
        if (forestResponse.isTimeout()) {
            return OperateResult.error("网络请求超时");
        }
        if (forestResponse.isError()) {
            return OperateResult.error("网络请求失败，状态码短语：" + forestResponse.getReasonPhrase() + "(" + forestResponse.getStatusCode() + ")");
        }

        // 5、提取响应结果
        ConversationResponse conversationResponse = null;
        try {
            conversationResponse = handleResponse(forestResponse, chatLog);
        } catch (Exception e) {
            return OperateResult.error("提取响应结果异常：" + e.getMessage());
        }
        // 6、保存日志
        try {
            saveChatLog(chatLog);
        } catch (Exception e) {
            log.error("保存日志异常：{}", e.getMessage(), e);
        }
        return OperateResult.success(conversationResponse);
    }

    /**
     * 组装日志对象
     *
     * @param conversationBody 会话请求体
     * @param request          请求对象
     * @return 操作结果
     */
    protected ModelChatLogDTO assembleLog(ConversationBodyDTO conversationBody, ForestRequest<?> request) {
        ModelChatLogDTO chatLog = new ModelChatLogDTO();
        chatLog.setModelChatId(Long.valueOf(conversationBody.getModel()));
        chatLog.setModelChatType(conversationBody.getApplicationType());
        chatLog.setSessionId(conversationBody.getSessionId());
        chatLog.setSendUserId(UserContextHolder.getUserId());
        String question = conversationBody.extractQuestion();
        chatLog.setSendMessage(question);
        chatLog.setStream(conversationBody.extractedStream());
        return chatLog;
    }


    /**
     * 处理响应结果
     *
     * @param response 响应结果
     * @return 操作结果
     */
    protected ConversationResponse handleResponse(ForestResponse<?> response, ModelChatLogDTO chatLog) {
        ConversationResponse conversationResponse = new ConversationResponse();
        ContentType contentType = response.getContentType();
        String original = response.readAsString();
        String extractedContent = null;
        int promptTokens;
        int completionTokens;
        // 处理流式响应
        if (StrUtil.equalsIgnoreCase("event-stream", contentType.getSubType())) {
            promptTokens = TokenCounterUtil.countTokens(chatLog.getSendMessage());
            completionTokens = StrUtil.count(original, "data: ") - 1;
            Matcher matcher = STREAM_PATTERN.matcher(original);
            // 构建结果字符串
            StringBuilder result = new StringBuilder();
            while (matcher.find()) {
                result.append(matcher.group(1));
            }
            extractedContent = result.toString();
        }
        // 非流式响应
        else if (BooleanUtil.isFalse(chatLog.getStream())) {
            extractedContent = extractedContent(original);
            JSONObject jsonObject = JSON.parseObject(original);
            JSONObject usage = jsonObject.getJSONObject("usage");
            promptTokens = usage.getInteger("prompt_tokens");
            completionTokens = usage.getInteger("completion_tokens");
        } else {
            promptTokens = 0;
            completionTokens = 0;
        }

        /* // 以 try-with-resource 方式读取流后，会自动关闭流
        try (InputStream in = response.getInputStream()) {
            // 从流中读取字符串数据
            String content = IoUtil.read(in, StandardCharsets.UTF_8);
            System.out.println(content);
        } catch (Exception ex) {
            ex.printStackTrace();
        } */
        conversationResponse.setOriginal(original);
        conversationResponse.setExtractedContent(extractedContent);
        Opt.ofNullable(chatLog).ifPresent(log -> {
            log.setResponseMessage(conversationResponse.getExtractedContent());
            log.setTokens(promptTokens, completionTokens);
        });
        return conversationResponse;
    }

    /**
     * 提取内容
     *
     * @param original 原始内容
     * @return 内容。
     */
    protected abstract String extractedContent(String original);

    /**
     * 保存日志
     *
     * @param chatLog 日志
     */
    protected void saveChatLog(ModelChatLogDTO chatLog) {
        Opt.ofNullable(chatLog).ifPresent(log -> taskExecutor.execute(() -> modelChatLogServiceClientFeign.add(log)));
    }
}