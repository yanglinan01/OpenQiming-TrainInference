package com.ctdi.cnos.llm.controller.metadata;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.ParameterizedTypeImpl;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.ctdi.cnos.llm.base.constant.CacheConstant;
import com.ctdi.cnos.llm.beans.log.chat.ModelChatLogDTO;
import com.ctdi.cnos.llm.beans.meta.constant.ApplicationType;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationBodyDTO;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationResponse;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTaskVO;
import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import com.ctdi.cnos.llm.config.ApplicationConfig;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.exception.MyRuntimeException;
import com.ctdi.cnos.llm.feign.log.ModelChatLogServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.DialogServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.ModelServiceClientFeign;
import com.ctdi.cnos.llm.feign.train.DeployTaskServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.TokenCounterUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

/**
 * 对话控制器。
 * <p>
 * 该控制器类负责处理与对话相关的HTTP请求，并将请求转发给相应的服务层进行处理。
 * </p>
 *
 * @author laiqi
 * @since 2024/9/14
 */
@Slf4j
@Api(tags = {"新模型体验对话"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/dialog")
public class DialogController {

    private final DialogServiceClientFeign dialogServiceClientFeign;
    private final ModelServiceClientFeign modelServiceClientFeign;
    private final DeployTaskServiceClientFeign deployTaskServiceClientFeign;
    private final ModelChatLogServiceClientFeign modelChatLogServiceClientFeign;

    /**
     * 默认缓存12小时
     */
    private final CtgCache ctgCache;

    @Qualifier(ApplicationConfig.MODEL_TASK_EXECUTOR)
    private final ThreadPoolTaskExecutor taskExecutor;

    private final WebClient webClient;

    private static final Type stringServerSentEventType = new ParameterizedTypeImpl(
            new Type[]{String.class}, // 实际类型参数
            null,                   // 所属类型，这里为 null 表示顶级类型
            ServerSentEvent.class               // 原始类型
    );

    private static final ParameterizedTypeReference<ServerSentEvent<String>> typeReference = ParameterizedTypeReference.forType(stringServerSentEventType);

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
        return dialogServiceClientFeign.conversation(conversationBody);
    }

    /**
     * 处理流式对话请求。
     * <p>
     * 该方法接收一个包含对话信息的请求体，并调用服务层的方法来处理对话逻辑。
     * 如果请求体验证通过，则返回处理结果；否则返回验证错误信息。
     * </p>
     *
     * @param conversationBody 包含对话信息的请求体
     * @return 操作结果，通常包含对话处理的结果信息
     */
    @ApiOperation(value = "SSE对话(仅支持流式)", notes = "SSE对话(仅支持流式)")
    @PostMapping(value = "/conversationStream")
    public SseEmitter conversationStream(@Validated @RequestBody ConversationBodyDTO conversationBody) {
        String requestId = StrUtil.emptyToDefault(conversationBody.getRequestId(), IdUtil.simpleUUID());
        log.info("收到requestId：{}请求参数: {}", requestId, JSON.toJSONString(conversationBody));
        // 设置超时时间为30分钟（1800000毫秒）
        SseEmitter emitter = new SseEmitter(1800000L);
        taskExecutor.execute(() -> {
            String url = null;
            try {
                String question = conversationBody.extractQuestion();
                JSONObject body = new JSONObject();
                // 自建模型
                if (ApplicationType.SELF_BUILT_MODEL == conversationBody.getApplicationType()) {
                    DeployTaskVO cacheModel = getCacheModel(conversationBody, DeployTaskVO.class, (bodyDTO) -> {
                        String modelId = bodyDTO.getModel();
                        return deployTaskServiceClientFeign.querySimpleById(modelId);
                    });
                    if (cacheModel != null) {
                        body = conversationBody.buildCustomerModelRequestBody();
                        url = cacheModel.getDeployUrl();
                    }
                }
                // 基础模型(系统模型)
                else if (ApplicationType.BIG_MODEL == conversationBody.getApplicationType()) {
                    ModelVO cacheModel = getCacheModel(conversationBody, ModelVO.class, (bodyDTO) -> {
                        String modelId = bodyDTO.getModel();
                        return modelServiceClientFeign.queryById(Long.valueOf(modelId));
                    });
                    if (cacheModel != null) {
                        body = conversationBody.buildCustomerModelRequestBody();
                        url = cacheModel.getEndpoint();
                    }
                }
                if (StrUtil.isNotEmpty(url)) {
                    ModelChatLogDTO chatLog = new ModelChatLogDTO();
                    chatLog.setModelChatType(conversationBody.getApplicationType());
                    chatLog.setModelChatId(Long.valueOf(conversationBody.getModel()));
                    chatLog.setSessionId(null);
                    chatLog.setSendUserId(UserContextHolder.getUserId());
                    chatLog.setSendMessage(question);
                    chatLog.setStream(true);
                    StringBuffer responseMessageBuffer = new StringBuffer();
                    AtomicInteger counter = new AtomicInteger();
                    webClient.method(HttpMethod.POST).uri(url)
                            .accept(MediaType.TEXT_EVENT_STREAM)
                            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .bodyValue(body.toJSONString())
                            .retrieve()
                            .bodyToFlux(typeReference)
                            .retryWhen(Retry.backoff(2, Duration.ofSeconds(5))) // 重试两次，每次间隔5秒
                            .doOnNext(event -> {
                                String data = event.data();
                                log.info("收到requestId：{}事件数据: {}", requestId, data);
                                counter.getAndIncrement();
                                if (ObjUtil.isNull(data)) {
                                    return;
                                }
                                try {
                                    emitter.send(event().data(data));
                                } catch (IOException e) {
                                    log.error("发送requestId：{}事件数据时发生错误1: {}", requestId, e.getMessage());
                                    // 如果发生异常，认为客户端已断开连接，结束发送
                                    emitter.completeWithError(e);
                                }
                                if (StrUtil.startWith(data, "[DONE]")) {
                                    // 结束了，存数据库
                                    emitter.complete();
                                    // 不要让次功能影响主业务
                                    try {
                                        chatLog.setResponseMessage(responseMessageBuffer.toString());
                                        // 计算token
                                        int promptTokens = TokenCounterUtil.countTokens(chatLog.getSendMessage());
                                        chatLog.setTokens(promptTokens, counter.decrementAndGet() - 1);
                                        taskExecutor.execute(() -> {
                                            modelChatLogServiceClientFeign.add(chatLog);
                                        });
                                    } catch (Exception e) {
                                        log.warn("保存模型体验日志异常", e);
                                    }
                                } else {
                                    Object contentObj = JSONPath.extract(data, "$.choices.delta.content");
                                    if (contentObj instanceof JSONArray) {
                                        JSONArray contentArray = (JSONArray) contentObj;
                                        contentArray.forEach(responseMessageBuffer::append);
                                    }
                                }
                            })
                            .onErrorResume(error -> {
                                log.error("发送requestId：{}事件数据时发生错误2: {}", requestId, error.getMessage());
                                // 如果发生异常，认为客户端已断开连接，结束发送
                                emitter.completeWithError(error);
                                return Flux.error(error);
                            })
                            .subscribe();// 完成Mono，不返回任何内容
                } else {
                    // 结束了，存数据库
                    emitter.complete();
                }
            } catch (Exception e) {
                // 过程中出现错误，立即中断
                log.error("发送requestId：{}流式对话过程发生异常！", e.getMessage(), e);
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    /**
     * 获取模型信息
     *
     * @param conversationBody 会话请求体
     * @return 模型信息
     */
    protected <M> M getCacheModel1(ConversationBodyDTO conversationBody, Class<M> clazz, Function<ConversationBodyDTO, M> buildModel) {
        Object modelData = ObjUtil.defaultIfNull(ctgCache.hget(CacheConstant.CONVERSATION_CACHE_KEY, conversationBody.getModel()), () -> {
            M model = buildModel.apply(conversationBody);
            ctgCache.hset(CacheConstant.CONVERSATION_CACHE_KEY, conversationBody.getModel(), model);
            return model;
        });
        if (modelData == null) {
            throw new MyRuntimeException("获取到的模型信息不存在！" + conversationBody.getModel());
        }
        if (modelData instanceof String) {
            log.info("查询缓存的类型是字符串，直接查库，模型信息：{}， 模型信息的类型：{}", modelData, modelData.getClass());
            return buildModel.apply(conversationBody);
        }
        return Convert.convert(clazz, modelData);
    }

    /**
     * 获取模型信息
     *
     * @param conversationBody 会话请求体
     * @return 模型信息
     */
    protected <M> M getCacheModel(ConversationBodyDTO conversationBody, Class<M> clazz, Function<ConversationBodyDTO, M> buildModel) {
        String modelId = conversationBody.getModel();
        String cacheId = conversationBody.getApplicationType() + ":" + modelId;
        Object modelData = ctgCache.getCache(CacheConstant.CONVERSATION_CACHE_KEY, cacheId);
        if (modelData == null) {
            M model = buildModel.apply(conversationBody);
            if (model != null) {
                ctgCache.set(CacheConstant.CONVERSATION_CACHE_KEY, cacheId, model, CacheConstant.CACHE_EXPIRE_1_DAY);
                return model;
            }
        }
        if (modelData == null) {
            throw new MyRuntimeException("获取到的模型信息不存在！" + modelId);
        }
        if (modelData instanceof String) {
            log.info("查询缓存的类型是字符串，直接查库！模型信息：{}， 模型信息的类型：{}", modelData, modelData.getClass());
            return buildModel.apply(conversationBody);
        }
        return Convert.convert(clazz, modelData);
    }
}