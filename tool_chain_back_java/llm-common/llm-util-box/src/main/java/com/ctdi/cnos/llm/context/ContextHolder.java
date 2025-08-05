package com.ctdi.cnos.llm.context;

import cn.hutool.core.thread.ThreadUtil;

/**
 * 上下文对象持有者。
 *
 * @author laiqi
 * @since 2024/7/16
 */
public class ContextHolder {

    /**
     * 当前上下文对象
     */
    private static final ThreadLocal<Object> CONTEXT_OBJECT = ThreadUtil.createThreadLocal(false);


    /**
     * 获得当前上下文对象。
     *
     * @return 当前上下文对象。
     */
    @SuppressWarnings("unchecked")
    public static <T> T get() {
        return (T) CONTEXT_OBJECT.get();
    }

    /**
     * 设置当前上下文对象。
     * @param contextObject 上下文对象。
     */
    public static void set(Object contextObject) {
        CONTEXT_OBJECT.set(contextObject);
    }

    /**
     * 清空上下文对象。
     */
    public static void clear() {
        CONTEXT_OBJECT.remove();
    }

}