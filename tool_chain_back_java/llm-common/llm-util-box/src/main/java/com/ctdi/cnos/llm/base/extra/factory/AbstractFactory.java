package com.ctdi.cnos.llm.base.extra.factory;

import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象的工厂类。
 *
 * @author laiqi
 * @since 2024/9/23
 */
@RequiredArgsConstructor
public abstract class AbstractFactory<T, E extends AbstractElement<T, ?, ?>> {

    /**
     * 元素集
     */
    private final List<E> elements;

    /**
     * 元素包装。
     */
    private final Map<T, E> elementCache = new ConcurrentHashMap<>();

    /**
     * 初始化包装。
     */
    @PostConstruct
    public void init() {
        elements.forEach(e -> elementCache.put(e.getType(), e));
    }

    /**
     * 根据类型获取元素。
     *
     * @param type 类型
     * @return 元素
     */
    public Optional<E> get(T type) {
        if (Objects.isNull(type)) {
            return Optional.empty();
        }
        return Optional.ofNullable(elementCache.get(type));
    }

}