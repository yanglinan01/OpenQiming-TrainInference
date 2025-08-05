package com.ctdi.cnos.llm.base.constant;

/**
 * 操作类型。
 *
 * @author laiqi
 * @since 2024/10/28
 */

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public enum ActionType {
    LIKE(1, "点赞"),
    DISLIKE(-1, "点踩"),
    Q1(1, "回答1"),
    Q2(2, "回答2");

    @Getter
    private final int value;
    @Getter
    private final String desc;
}