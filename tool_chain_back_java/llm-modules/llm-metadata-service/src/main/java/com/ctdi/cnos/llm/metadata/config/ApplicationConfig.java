/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * metadata 配置
 *
 * @author huangjinhua
 * @since 2024/10/17
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "metadata")
public class ApplicationConfig {

    /**
     * 运营中心配置
     */
    private OperationCenter operationCenter;

    /**
     * 策选接口 策选查询
     */
    private String strategyListUrl;

    /**
     * 发送评测中任务最大限制
     */
    private String maxEvaluationTaskLimit;
}
