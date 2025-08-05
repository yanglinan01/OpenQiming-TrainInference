/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 大模型门户权限开通回调接口配置
 *
 * @author wangyy
 * @since 2024-12-6
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "llm-oa-access-granted-callback")
public class LlmOaAccessGrantedCallBackConfig {

    /**
     * 接口url
     */
    private String url;

    /**
     * X-APP-ID
     */
    private String xAppId;

    /**
     * X-APP-KEY
     */
    private String xAppKey;

}
