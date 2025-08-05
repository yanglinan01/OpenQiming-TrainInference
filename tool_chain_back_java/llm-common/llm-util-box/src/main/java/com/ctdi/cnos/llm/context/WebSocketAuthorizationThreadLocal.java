package com.ctdi.cnos.llm.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 线程本地化数据管理的工具类。可根据需求自行添加更多的线程本地化变量及其操作方法。。
 *
 * @author laiqi
 * @since 2024/6/18
 */
public class WebSocketAuthorizationThreadLocal {
    /**
     * 存储websocket连接中的会话的线程本地化对象。
     */
    private static final TransmittableThreadLocal<String> WS_AUTHORIZATION = new TransmittableThreadLocal<>();

    /**
     * 设置websocket连接中的会话。
     *
     * @param authorization token。
     * @return 返回之前的状态，便于恢复。
     */
    public static String setWsAuthorization(String authorization) {
        String oldValue = WS_AUTHORIZATION.get();
        WS_AUTHORIZATION.set(authorization);
        return oldValue;
    }

    /**
     * 获取websocket连接中的会话。
     * @return websocket连接中的会话。
     */
    public static String getWsAuthorization() {
        return WS_AUTHORIZATION.get();
    }

    /**
     * 清空该存储数据，主动释放线程本地化存储资源。
     */
    public static void clearDataFilter() {
        WS_AUTHORIZATION.remove();
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private WebSocketAuthorizationThreadLocal() {
    }
}