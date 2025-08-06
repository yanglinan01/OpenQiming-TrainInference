package com.ctdi.cnos.llm.code.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 。
 *
 * @author laiqi
 * @since 2024/7/19
 */
@Getter
@RequiredArgsConstructor
public enum SchemaEnum {
    META("meta", "meta", "metadata", "METADATA"),
    REASON("reason","reason","reason", "REASON"),
    LOG("log","log","log", "LOG"),
    TRAIN("train","train","train", "TRAIN"),
    REGISTER("register", "register", "register", "REGISTER"),
    API("api","api","api", "API");

    private static final Map<String, SchemaEnum> DICT_MAP = new HashMap<>(3);
    static {
        DICT_MAP.put(META.dbScheme, META);
        DICT_MAP.put(REASON.dbScheme, REASON);
        DICT_MAP.put(LOG.dbScheme, LOG);
        DICT_MAP.put(TRAIN.dbScheme, TRAIN);
        DICT_MAP.put(REGISTER.dbScheme, REGISTER);
        DICT_MAP.put(API.dbScheme, API);
    }


    private final String dbScheme;
    private final String entityPackage;
    private final String modulePackage;
    private final String feignClientRef;

    public static SchemaEnum getByDbScheme(String dbScheme) {
        return DICT_MAP.get(dbScheme);
    }
}