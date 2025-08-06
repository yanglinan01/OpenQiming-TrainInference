package com.ctdi.cnos.llm.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 启动打印Nacos服务器信息。
 *
 * @author laiqi
 * @since 2024/11/11
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class NacosConfigInitializer {
    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    @PostConstruct
    public void init() {
        // 帮我打印一下关键的信息就好
        log.info("Nacos服务器地址：{}", nacosDiscoveryProperties.getServerAddr());
        log.info("Nacos命名空间：{}", nacosDiscoveryProperties.getNamespace());
        log.info("Nacos服务名：{}", nacosDiscoveryProperties.getService());
        log.info("Nacos服务实例：{}", nacosDiscoveryProperties.getIp());
    }
}