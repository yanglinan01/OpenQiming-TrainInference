package com.ctdi.cnos.llm.log.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author caojunhao
 * @DATE 2024/7/5
 */
@EnableAsync
@Slf4j
@Configuration
public class ThreadPoolConfig {
    /**
     * 核心线程数
     */
    private int corePoolSize = 8;
    /**
     * 最大线程数
     */
    private int maxPoolSize = 16;
    /**
     * 队列大小
     */
    private int queueCapacity = 100;
    /**
     * 设置线程活跃时间(秒)
     */
    private int keepAliveSeconds = 60;

    @Bean(name = "modelMonitorTaskExecutor")
    public ThreadPoolTaskExecutor modelMonitorTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize); // 核心线程数
        executor.setMaxPoolSize(maxPoolSize); // 最大线程数
        executor.setQueueCapacity(queueCapacity); // 队列容量
        executor.setThreadNamePrefix("ModelMonitor-");
        executor.initialize();
        return executor;
    }
}
