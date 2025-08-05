package com.ctdi.cnos.llm.beans.meta.dialog;

/**
 * 会话内容类型。
 *
 * @author laiqi
 * @since 2024/11/1
 */
public enum ConversationContentType {
    /**
     * 文本。
     */
    TEXT,
    /**
     * 代理类型。即直接代理messages内容。不做封装。
     */
    PROXY;
}