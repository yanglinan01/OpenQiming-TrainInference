package com.ctdi.cnos.llm.websocket.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.ctdi.cnos.llm.beans.log.chat.ModelChatLogDTO;
import com.ctdi.cnos.llm.beans.meta.application.ApplicationSquareVO;
import com.ctdi.cnos.llm.beans.meta.constant.ApplicationType;
import com.ctdi.cnos.llm.beans.reason.req.ModelChatRequest;
import com.ctdi.cnos.llm.config.WebConfig;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.websocket.context.ModelExperienceCache;
import com.ctdi.cnos.llm.websocket.context.WebSocketSessionManager;
import com.ctdi.cnos.llm.websocket.util.WebSocketUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

/**
 * 知识助手WebSocket代理服务端处理器(接收前端消息并转发到大模型处理)。
 *
 * @author laiqi
 * @since 2024/6/7
 */
@RequiredArgsConstructor
@Slf4j
public class KnowledgeAssistantWebSocketProxyServerHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 获取应用广场ID
        Long applicationSquareId = WebSocketUtil.getApplicationSquareId(session, false);
        UserVO user = WebSocketUtil.getUser(session);
        log.info("已建立前端的连接: {}, 应用广场ID：{}", session.getId(), applicationSquareId);
        // 分配知识助手连接
        if (ObjUtil.hasNull(applicationSquareId, user)) {
            log.info("前端信息请求有误！应用广场ID或者用户不存在!");
            return;
        }
        WebSocketUtil.openConnection(session);
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String messageStr = message.getPayload();
        log.info("已接收前端的消息: {}, {}", session.getId(), messageStr);
        String modelChatRequestMessage;
        try {
            modelChatRequestMessage = messageWrapper(session, message);
        } catch (Exception e) {
            log.info("前端信息请求包装有误！异常原因：{}, 消息：{}", e.getMessage(), messageStr, e);
            String response = "提交的信息有误!请重新提交~";
            WebSocketUtil.sendMessage(session, response);
            //Optional.ofNullable(WebSocketSessionManager.getModelChatLogDto(session)).ifPresent(log -> log.setResponseMessage(response));
            return;
        }
        WebSocketSession proxySession = WebSocketSessionManager.getProxySession(session);
        if (BooleanUtil.isFalse(WebSocketUtil.sendMessage(proxySession, modelChatRequestMessage))) {
            WebSocketUtil.sendMessage(session, "发送消息失败");
        }
    }

    /**
     * 消息包装
     *
     * @param session 会话
     * @param message 前端消息
     * @return 包装之后的消息
     * @throws Exception
     */
    public String messageWrapper(WebSocketSession session, TextMessage message) throws Exception {
        String messageStr = message.getPayload();
        Long applicationSquareId = WebSocketUtil.getApplicationSquareId(session, false);
        ApplicationSquareVO applicationSquareVO = ModelExperienceCache.getByIdWithWebSocket(true, applicationSquareId, session);
        if (Objects.isNull(applicationSquareVO)) {
            throw new RuntimeException("获取应用广场信息失败");
        }
        ModelChatRequest modelChatRequest = JSON.parseObject(messageStr, ModelChatRequest.class);
        modelChatRequest.setSession_id(session.getId());
        // 获取用户区域
        UserVO user = WebSocketUtil.getUser(session);
        String regionCode = Objects.requireNonNull(user).getRegionCode();
        modelChatRequest.setUid(applicationSquareVO.getUid());
        modelChatRequest.setProv(regionCode);
        modelChatRequest.setScene(Integer.parseInt(applicationSquareVO.getScene()));
        WebConfig config = SpringUtil.getBean(WebConfig.class);
        if (config.isEnableModelChatLog()) {
            ModelChatLogDTO chatLog = new ModelChatLogDTO();
            chatLog.setModelChatType(ApplicationType.KNOWLEDGE_ASSISTANT);
            chatLog.setModelChatId(applicationSquareId);
            chatLog.setSessionId(session.getId());
            chatLog.setSendUserId(Convert.toLong(user.getId()));
            chatLog.setSendMessage(modelChatRequest.getParam1());
            WebSocketSessionManager.addSessionModelChatLogDto(session, chatLog);
        }
        return JSON.toJSONString(modelChatRequest);
        // return JSON.parseObject(preprocessMessages).fluentPutAll(JSON.parseObject(payload)).toJSONString();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("已关闭前端的连接: {}", session.getId());
        // 关闭原生会话
        WebSocketSessionManager.closeConnection(session);

        // 获取并尝试关闭代理会话
        WebSocketSessionManager.closeConnection(WebSocketSessionManager.getProxySession(session));

        WebSocketSessionManager.removeSession(session);
    }

}