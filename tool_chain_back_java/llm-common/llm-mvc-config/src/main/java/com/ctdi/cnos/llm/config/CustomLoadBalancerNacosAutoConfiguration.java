package com.ctdi.cnos.llm.config;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author: liub
 * @date: 2023-10-08 19:29
 * @desc:
 */
@Configuration(
        proxyBeanMethods = false
)
@EnableConfigurationProperties
@ConditionalOnProperty(
        value = {"spring.cloud.loadbalancer.nacos.enabled"},
        havingValue = "false"
)
@ConditionalOnNacosDiscoveryEnabled
@LoadBalancerClients(
        defaultConfiguration = {NacosLocalFirstLoadBalancerConfiguration.class}
)
public class CustomLoadBalancerNacosAutoConfiguration {
}
