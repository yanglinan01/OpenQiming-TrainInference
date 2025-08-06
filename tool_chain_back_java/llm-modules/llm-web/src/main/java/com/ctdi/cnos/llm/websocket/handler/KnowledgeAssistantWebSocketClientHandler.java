package com.ctdi.cnos.llm.websocket.handler;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ctdi.cnos.llm.beans.log.chat.ModelChatLogDTO;
import com.ctdi.cnos.llm.config.WebConfig;
import com.ctdi.cnos.llm.constant.Constants;
import com.ctdi.cnos.llm.context.WebSocketAuthorizationThreadLocal;
import com.ctdi.cnos.llm.feign.log.ModelChatLogServiceClientFeign;
import com.ctdi.cnos.llm.websocket.context.WebSocketSessionManager;
import com.ctdi.cnos.llm.websocket.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 知识助手WebSocket客户端处理器(转发消息)。
 *
 * @author laiqi
 * @since 2024/6/7
 */
@Slf4j
public class KnowledgeAssistantWebSocketClientHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession proxySession) throws Exception {
        // log.info("知识助手客户端连接已建立: {}", proxySession.getId());
    }


    @Override
    protected void handleTextMessage(WebSocketSession proxySession, TextMessage message) throws Exception {
        String messageStr = message.getPayload();
        log.info("知识助手客户端已接收消息: {}, {}", proxySession.getId(), messageStr);
        WebSocketSession session = WebSocketSessionManager.getSession(proxySession);
        if (BooleanUtil.isFalse(WebSocketUtil.sendMessage(session, messageStr))) {
            log.warn("知识助手客户端代理转发消息失败!");
        }
        WebConfig config = SpringUtil.getBean(WebConfig.class);
        if (config.isEnableModelChatLog()) {
            ModelChatLogDTO chatLog = WebSocketSessionManager.getModelChatLogDto(session);
            if (StrUtil.endWith(messageStr, Constants.MODEL_CHAT_END)) {
                messageStr = StrUtil.removeSuffix(messageStr, Constants.MODEL_CHAT_END);
                chatLog.appendResponseMessage(messageStr, true);
                try {
                    WebSocketAuthorizationThreadLocal.setWsAuthorization(WebSocketUtil.getAuthorization(session, false));
                    SpringUtil.getBean(ModelChatLogServiceClientFeign.class).add(chatLog);
                } catch (Exception e) {
                    log.warn("保存知识助手模型体验日志异常", e);
                }finally {
                    WebSocketAuthorizationThreadLocal.clearDataFilter();
                }
            } else {
                chatLog.appendResponseMessage(messageStr);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("知识助手客户端连接已关闭: {}", session.getId());
    }
}