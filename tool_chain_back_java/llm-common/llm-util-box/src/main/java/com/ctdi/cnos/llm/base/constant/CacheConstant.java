/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.base.constant;

/**
 * 系统常量
 *
 * @author huangjinhua
 * @since 2024/8/2
 */
public interface CacheConstant {

    /**
     * 缓存用户信息的缓存名
     */
    String USER_CACHE_NAME = "userCache";

    /**
     * 字典缓存名
     */
    String DICTIONARY_CACHE_NAME = "DICTIONARY_CACHE";
    /**
     * 省份缓存名
     */
    String PROVINCE_CACHE_NAME = "PROVINCE_CACHE";
    /**
     * 省份缓存key
     */
    String PROVINCE_CACHE_KEY = "province-list";

    /**
     * 集群缓存名
     */
    String CLUSTER_CACHE_NAME = "CLUSTER_CACHE";

    /**
     * 会话缓存名
     */
    String CONVERSATION_CACHE_KEY = "CONVERSATION_CACHE";
    /**
     * 缓存失效时间 1天
     */
    int CACHE_EXPIRE_1_DAY = 24 * 60 * 60;
    /**
     * 缓存失效时间 12 小时
     */
    int CACHE_EXPIRE_12_HOUR = 12 * 60 * 60;
    /**
     * 缓存失效时间 1 小时
     */
    int CACHE_EXPIRE_1_HOUR = 60 * 60;

}