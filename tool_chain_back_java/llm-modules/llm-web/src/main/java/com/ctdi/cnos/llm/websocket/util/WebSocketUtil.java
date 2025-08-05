package com.ctdi.cnos.llm.websocket.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import com.ctdi.cnos.llm.system.auth.AuthClient;
import com.ctdi.cnos.llm.beans.meta.application.ApplicationSquareVO;
import com.ctdi.cnos.llm.constant.Constants;
import com.ctdi.cnos.llm.context.WebSocketAuthorizationThreadLocal;
import com.ctdi.cnos.llm.feign.metadata.ApplicationSquareServiceClientFeign;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.websocket.context.ModelExperienceCache;
import com.ctdi.cnos.llm.websocket.context.WebSocketSessionManager;
import com.ctdi.cnos.llm.websocket.handler.KnowledgeAssistantWebSocketClientHandler;
import com.ctdi.cnos.llm.websocket.handler.KnowledgeAssistantWebSocketConnectionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 。
 *
 * @author laiqi
 * @since 2024/6/11
 */
@Slf4j
public class WebSocketUtil {

    /**
     * 前端连接建立时，前端会携带应用广场ID，用于分配大模型连接
     */
    public static final String APPLICATION_SQUARE_ID_HEADER_KEY = "Application-Square-Id";

    /**
     * 前端连接建立时，前端会携带应用广场ID，用于分配大模型连接
     */
    public static final String APPLICATION_SQUARE_ID_PARAM_KEY = "applicationSquareId";

    public static Map<Long, KnowledgeAssistantWebSocketConnectionManager> applicationSquareWebSocketConnectionManagers = new ConcurrentHashMap<>();

    /**
     * 从请求头中获取应用广场ID
     *
     * @param session
     * @return
     */
    public static Long getApplicationSquareId(WebSocketSession session, boolean formHeader) {
        // 获取应用广场ID
        return NumberUtil.parseLong(formHeader ? getWebSocketSessionHeader(session, APPLICATION_SQUARE_ID_HEADER_KEY) : getWebSocketSessionQuery(session, APPLICATION_SQUARE_ID_PARAM_KEY), null);
    }

    /**
     * 从请求头中获取Token
     *
     * @param session
     * @return
     */
    public static String getAuthorization(WebSocketSession session, boolean formHeader) {
        return formHeader ? getWebSocketSessionHeader(session, Constants.AUTH_HEADER_KEY) : getWebSocketSessionQuery(session, Constants.AUTH_HEADER_KEY);
    }

    /**
     * 从URL中的查询参数中提取指定的参数
     *
     * @param session   会话
     * @param parameter 参数名
     * @return
     */
    public static String getWebSocketSessionQuery(WebSocketSession session, String parameter) {
        Map<String, String> paramMap = HttpUtil.decodeParamMap(Objects.requireNonNull(session.getUri()).getQuery(), CharsetUtil.CHARSET_UTF_8);
        if (!paramMap.containsKey(parameter)) {
            log.error("前端连接建立失败，链接中未获取到{}信息", parameter);
            return null;
        }
        return paramMap.get(parameter);
    }

    /**
     * 从请求头中获取指定header信息
     *
     * @param session 会话
     * @param header  头
     * @return
     */
    public static String getWebSocketSessionHeader(WebSocketSession session, String header) {
        HttpHeaders handshakeHeaders = session.getHandshakeHeaders();
        if (!handshakeHeaders.containsKey(header)) {
            log.error("前端连接建立失败，请求头中未获取到{}信息", header);
            return null;
        }
        return handshakeHeaders.getFirst(header);
    }

    /**
     * 从请求头中获取用户信息
     *
     * @param session
     * @return
     */
    public static UserVO getUser(WebSocketSession session) {
        String token = getAuthorization(session, false);
        AuthClient authClient = SpringUtil.getBean(AuthClient.class);
        return authClient.getUser(token);
    }

    /**
     * 为获取大模型连接
     *
     * @param session 会话
     */
    public static void openConnection(WebSocketSession session) {
        Long applicationSquareId = getApplicationSquareId(session, false);
        if (Objects.nonNull(applicationSquareId)) {
            KnowledgeAssistantWebSocketConnectionManager connectionManager = applicationSquareWebSocketConnectionManagers.computeIfAbsent(applicationSquareId, id -> {
                ApplicationSquareVO applicationSquareVO = ModelExperienceCache.getByIdWithWebSocket(true, id, session);
                if (Objects.isNull(applicationSquareVO)) {
                    throw new RuntimeException("获取应用广场信息失败");
                }
                StandardWebSocketClient client = new StandardWebSocketClient();
                KnowledgeAssistantWebSocketClientHandler webSocketHandler = new KnowledgeAssistantWebSocketClientHandler();
                KnowledgeAssistantWebSocketConnectionManager manager = new KnowledgeAssistantWebSocketConnectionManager(client, webSocketHandler, applicationSquareVO.getEndpoint());
                // manager.getHeaders().set("X-APP-ID", properties.getAppId());
                // manager.getHeaders().set("X-APP-KEY", properties.getAppKey());
                return manager;
            });
            // ModelWebSocketConnectionManager connectionManager = SpringUtil.getBean(ModelWebSocketConnectionManager.class);
            connectionManager.openConnection(new ListenableFutureCallback<WebSocketSession>() {
                @Override
                public void onFailure(Throwable ex) {
                    log.error("分配启明网络大模型连接失败! session = {}, message = {}", session.getId(), ex.getMessage(), ex);
                }

                @Override
                public void onSuccess(WebSocketSession result) {
                    log.info("分配启明网络大模型连接成功! session = {}, proxySession = {}", session.getId(), result.getId());
                    WebSocketSessionManager.addSession(session, result);
                }
            });
        }
    }

    /**
     * 发送消息
     *
     * @param session 会话
     * @param message 消息体
     * @return 是否成功
     */
    public static boolean sendMessage(WebSocketSession session, String message) {
        if (WebSocketSessionManager.isConnected(session)) {
            try {
                session.sendMessage(new TextMessage(message));
                return true;
            } catch (IOException e) {
                log.warn("发送消息失败!{}", e.getMessage(), e);
            }
        }
        log.warn("session为空或者session未打开");
        return false;
    }

    /**
     * 根据token和id查询应用广场信息
     *
     * @param token
     * @param id
     * @return
     */
    public static ApplicationSquareVO queryApplicationSquareById(String token, Long id) {
        WebSocketAuthorizationThreadLocal.setWsAuthorization(token);
        try {
            ApplicationSquareServiceClientFeign clientFeign = SpringUtil.getBean(ApplicationSquareServiceClientFeign.class);
            return clientFeign.queryById(id);
        } catch (Exception e) {
            log.error("调用应用广场接口失败！{}", e.getMessage(), e);
        } finally {
            WebSocketAuthorizationThreadLocal.clearDataFilter();
        }
        return null;
    }
}