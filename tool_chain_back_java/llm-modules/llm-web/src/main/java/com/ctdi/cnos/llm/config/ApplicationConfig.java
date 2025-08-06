/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.config;

import cn.hutool.core.map.MapUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 配置文件
 *
 * @author huangjinhua
 * @since 2024/7/10
 */
@Getter
@Setter
@Configuration
public class ApplicationConfig {

    public final static String MODEL_TASK_EXECUTOR = "modelTaskExecutor";
    @Bean(name = MODEL_TASK_EXECUTOR)
    public ThreadPoolTaskExecutor applicationTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(50); // 调整核心线程池大小为50
        taskExecutor.setMaxPoolSize(200); // 调整最大线程池大小为200
        taskExecutor.setQueueCapacity(100); // 设置队列容量为100
        taskExecutor.setThreadNamePrefix("ModelTaskExecutor-"); // 为线程设置名称前缀
        taskExecutor.setThreadFactory(new CustomizableThreadFactory("modelTaskExecutor")); // 自定义线程工厂
        taskExecutor.setKeepAliveSeconds(60); // 设置线程的空闲存活时间
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 设置拒绝策略为CallerRunsPolicy
        return taskExecutor;
    }
}