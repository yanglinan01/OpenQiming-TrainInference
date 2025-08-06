package com.ctdi.cnos.llm.metadata.extra.conversation;

import cn.hutool.extra.spring.SpringUtil;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationBodyDTO;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationResponse;
import com.ctdi.cnos.llm.exception.MyRuntimeException;
import com.ctdi.cnos.llm.response.OperateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Optional;

/**
 * 对话处理器 工厂。
 *
 * @author laiqi
 * @since 2024/9/14
 */
@Component
@RequiredArgsConstructor
public class ConversationProcessorFactory {

    private static Collection<BaseConversationProcessor> conversationProcessors;

    @PostConstruct
    public void init() {
        ConversationProcessorFactory.conversationProcessors = SpringUtil.getBeansOfType(BaseConversationProcessor.class).values();
    }

    /**
     * 匹配处理器。
     *
     * @param conversationBody 会话请求体
     * @return 处理器
     */
    public static BaseConversationProcessor<?> matchConversationProcessor(ConversationBodyDTO conversationBody) {
        for (BaseConversationProcessor<?> conversationProcessor : conversationProcessors) {
            if (conversationProcessor.match(conversationBody)) {
                return conversationProcessor;
            }
        }
        return null;
    }

    /**
     * 调用处理器。
     * @param conversationBody 会话请求体
     * @return 处理结果
     */
    public static OperateResult<ConversationResponse> callConversationProcessor(ConversationBodyDTO conversationBody) {
        return Optional.ofNullable(matchConversationProcessor(conversationBody)).orElseThrow(() -> new MyRuntimeException("未匹配到合适的处理器")).process(conversationBody);
    }
}