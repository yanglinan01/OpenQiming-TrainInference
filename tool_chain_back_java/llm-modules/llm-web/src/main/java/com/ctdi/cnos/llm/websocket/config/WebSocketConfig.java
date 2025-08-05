package com.ctdi.cnos.llm.websocket.config;

import com.ctdi.cnos.llm.feign.log.ModelChatLogServiceClientFeign;
import com.ctdi.cnos.llm.websocket.handler.KnowledgeAssistantWebSocketProxyServerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.support.WebSocketHttpRequestHandler;

/**
 * WebSocket配置类。
 *
 * @author laiqi
 * @since 2024/6/7
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ModelChatLogServiceClientFeign logServiceClientFeign;
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public WebSocketHttpRequestHandler webSocketHttpRequestHandler() {
        WebSocketHttpRequestHandler websocketHandler = new WebSocketHttpRequestHandler(modelProxyWebSocketHandler());
        // 可以在这里添加握手拦截器等配置
        return websocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 知识助手WebSocket处理器
        registry.addHandler(modelProxyWebSocketHandler(), "/ws/qiming_scene").setAllowedOrigins("*")/* .withSockJS() */;
    }

    /**
     * 模型代理 WebSocket 处理器。
     * @return
     */
    @Bean
    public WebSocketHandler modelProxyWebSocketHandler() {
        return new KnowledgeAssistantWebSocketProxyServerHandler();
    }

    // /**
    //  * WebSocket 连接管理器。适用于单个连接的情况下。
    //  * @param properties
    //  * @return
    //  */
    // @ConditionalOnMissingBean(ModelWebSocketConnectionManager.class)
    // @Bean
    // public WebSocketConnectionManager webSocketConnectionManager(ModelProperties properties) {
    //     StandardWebSocketClient client = new StandardWebSocketClient();
    //     ModelWebSocketClientHandler webSocketHandler = new ModelWebSocketClientHandler();
    //     WebSocketConnectionManager manager = new WebSocketConnectionManager(client, webSocketHandler, properties.getServerUrl());
    //     manager.getHeaders().set("X-APP-ID", properties.getAppId());
    //     manager.getHeaders().set("X-APP-KEY", properties.getAppKey());
    //     manager.setAutoStartup(false);
    //     return manager;
    // }
    //
    // /**
    //  * 模型 WebSocket 连接管理器。适用于多个连接的情况下。
    //  * @param properties
    //  * @return
    //  */
    // @Bean
    // public ModelWebSocketConnectionManager modelWebSocketConnectionManager(ModelProperties properties){
    //     StandardWebSocketClient client = new StandardWebSocketClient();
    //     ModelWebSocketClientHandler webSocketHandler = new ModelWebSocketClientHandler();
    //     ModelWebSocketConnectionManager manager = new ModelWebSocketConnectionManager(client, webSocketHandler, properties.getServerUrl());
    //     manager.getHeaders().set("X-APP-ID", properties.getAppId());
    //     manager.getHeaders().set("X-APP-KEY", properties.getAppKey());
    //     return manager;
    // }
}