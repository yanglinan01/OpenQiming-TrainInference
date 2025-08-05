package com.ctdi.cnos.llm.base.extra.factory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 抽象的元素。
 *
 * @author laiqi
 * @since 2024/9/23
 *
 * @param <T> 元素类型。
 * @param <P> 参数类型。
 * @param <R> 返回类型。
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractElement<T, P, R> {
    /**
     * 类型。
     */
    private final T type;

    public R doExecute(P params) {
        return execute(params);
    }

    /**
     * 执行。
     *
     * @param params
     * @return
     */
    protected abstract R execute(P params);
}