package com.ctdi.cnos.llm.base.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 统计类型。
 *
 * @author laiqi
 * @since 2024/10/17
 */
@Getter
@RequiredArgsConstructor
public enum StatType {
    /**
     * 当天统计
     */
    DAY("DAY"),
    /**
     * 当月统计
     */
    MONTH("MONTH"),
    /**
     * 累计统计
     */
    ALL("ALL");

    /**
     * 统计类型
     */
    private final String label;

}