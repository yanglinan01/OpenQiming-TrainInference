package com.ctdi.cnos.llm.metadata.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Slf4j
@Configuration
@Data
@ConfigurationProperties(prefix = "file-upload.thread-pool")
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


    @Bean(name = "fileUploadExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        log.info("fileUploadExecutor线程池参数：corePoolSize:{}, maxPoolSize:{}, queueCapacity:{}, keepAliveSeconds:{}",
                corePoolSize, maxPoolSize, queueCapacity, keepAliveSeconds);

        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("FileUpload-"); // 线程前缀名
        executor.initialize();
        return executor;
    }
}