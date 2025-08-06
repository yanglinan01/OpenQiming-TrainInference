package com.ctdi.cnos.llm.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.LoggingWebSocketHandlerDecorator;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * 自定义WebSocket连接管理器。
 *
 * @author laiqi
 * @see org.springframework.web.socket.client.WebSocketConnectionManager
 * @since 2024/6/11
 */
@Slf4j
public class KnowledgeAssistantWebSocketConnectionManager {

    private final URI uri;
    private final WebSocketClient client;

    private final WebSocketHandler webSocketHandler;

    private WebSocketHttpHeaders headers = new WebSocketHttpHeaders();


    public KnowledgeAssistantWebSocketConnectionManager(WebSocketClient client,
                                                        WebSocketHandler webSocketHandler, String uriTemplate, Object... uriVariables) {
        this.uri = UriComponentsBuilder.fromUriString(uriTemplate).buildAndExpand(
                uriVariables).encode().toUri();
        this.client = client;
        this.webSocketHandler = decorateWebSocketHandler(webSocketHandler);
    }


    /**
     * Decorate the WebSocketHandler provided to the class constructor.
     * <p>By default {@link LoggingWebSocketHandlerDecorator} is added.
     */
    protected WebSocketHandler decorateWebSocketHandler(WebSocketHandler handler) {
        return new LoggingWebSocketHandlerDecorator(handler);
    }

    /**
     * Set the sub-protocols to use. If configured, specified sub-protocols will be
     * requested in the handshake through the {@code Sec-WebSocket-Protocol} header. The
     * resulting WebSocket session will contain the protocol accepted by the server, if
     * any.
     */
    public void setSubProtocols(List<String> protocols) {
        this.headers.setSecWebSocketProtocol(protocols);
    }

    /**
     * Return the configured sub-protocols to use.
     */
    public List<String> getSubProtocols() {
        return this.headers.getSecWebSocketProtocol();
    }

    /**
     * Set the origin to use.
     */
    public void setOrigin(@Nullable String origin) {
        this.headers.setOrigin(origin);
    }

    /**
     * Return the configured origin.
     */
    @Nullable
    public String getOrigin() {
        return this.headers.getOrigin();
    }

    /**
     * Provide default headers to add to the WebSocket handshake request.
     */
    public void setHeaders(HttpHeaders headers) {
        this.headers.clear();
        this.headers.putAll(headers);
    }

    /**
     * Return the default headers for the WebSocket handshake request.
     */
    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public void openConnection(ListenableFutureCallback<WebSocketSession> callback) {
        if (log.isInfoEnabled()) {
            log.debug("准备WebSocket握手连接：{}", this.uri);
        }
        ListenableFuture<WebSocketSession> future =
                this.client.doHandshake(this.webSocketHandler, this.headers, this.uri);
        future.addCallback(callback);
    }
}