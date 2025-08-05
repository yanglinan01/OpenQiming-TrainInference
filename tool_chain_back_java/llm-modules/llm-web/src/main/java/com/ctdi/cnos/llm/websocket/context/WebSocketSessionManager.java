package com.ctdi.cnos.llm.websocket.context;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.BiMap;
import cn.hutool.core.util.BooleanUtil;
import com.ctdi.cnos.llm.beans.log.chat.ModelChatLogDTO;
import com.ctdi.cnos.llm.websocket.util.WebSocketUtil;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 会话管理器。
 *
 * @author laiqi
 * @since 2024/6/11
 */
public class WebSocketSessionManager {

    /**
     * 会话映射。key为本地服务会话，value为代理会话
     */
    public static BiMap<WebSocketSession, WebSocketSession> sessionsMap = new BiMap<>(new ConcurrentHashMap<>());

    public static Map<WebSocketSession, ModelChatLogDTO> sessionModelChatLogDtoMap = new ConcurrentHashMap<>();

    /**
     * 添加session
     *
     * @param session 会话
     * @param proxySession 代理会话
     */
    public static void addSession(WebSocketSession session, WebSocketSession proxySession) {
        sessionsMap.put(session, proxySession);
    }

    public static void addSessionModelChatLogDto(WebSocketSession session, ModelChatLogDTO modelChatLogDto) {
        sessionModelChatLogDtoMap.put(session, modelChatLogDto);
    }

    public static ModelChatLogDTO getModelChatLogDto(WebSocketSession session) {
        return sessionModelChatLogDtoMap.get(session);
    }

    /**
     * 获取代理session
     *
     * @param session 会话
     * @return 代理会话
     */
    public static WebSocketSession getProxySession(WebSocketSession session) {
        WebSocketSession proxySession = sessionsMap.get(session);
        if (isDisconnected(proxySession)) {
            WebSocketUtil.openConnection(session);
        }
        return sessionsMap.get(session);
    }

    /**
     * 获取会话
     *
     * @param session 代理session
     * @return 会话
     */
    public static WebSocketSession getSession(WebSocketSession session) {
        return sessionsMap.getKey(session);
    }

    /**
     * 移除session 并触发一次清理
     *
     * @param session 会话
     */
    public static void removeSession(WebSocketSession session) {
        sessionsMap.remove(session);
        // 移除已关闭的会话，使用Java 8 Stream方式筛选
        Set<WebSocketSession> sessionsToBeRemoved = sessionsMap.keySet()
                .stream()
                .filter(WebSocketSessionManager::isDisconnected)
                .collect(Collectors.toCollection(HashSet::new));

        // 确保线程安全地从map中移除会话
        sessionsToBeRemoved.forEach(temp -> sessionsMap.remove(temp));
    }

    /**
     * 判断会话是否连接
     * @param session 会话
     * @return 是否连接
     */
    public static boolean isConnected(WebSocketSession session) {
        return (session != null && session.isOpen());
    }

    /**
     * 判断会话是否断开
     * @param session 会话
     * @return 是否断开
     */
    public static boolean isDisconnected(WebSocketSession session) {
        return BooleanUtil.isFalse(isConnected(session));
    }

    /**
     * 关闭会话
     * @param session 会话
     * @throws Exception
     */
    public static void closeConnection(WebSocketSession session) {
        if (session != null) {
            IoUtil.close(session);
        }
    }
}