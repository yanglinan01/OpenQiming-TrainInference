/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置信息
 *
 * @author huangjinhua
 * @since 2024/6/4
 */
@Configuration
@ConfigurationProperties(prefix = "job")
@Getter
@Setter
public class ApplicationConfig {
    /**
     * 是否启动自动执行
     */
    private boolean autoStartUp;
}
