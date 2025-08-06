package com.ctdi.cnos.llm.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.ctdi.cnos.llm.loadbalancer.NacosLocalFirstLoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * nacos 负载均衡同IP 同区域有限
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnDiscoveryEnabled
public class NacosLocalFirstLoadBalancerConfiguration {
    private static final Logger log = LoggerFactory.getLogger(NacosLocalFirstLoadBalancerConfiguration.class);

    /**
     * 优先路由配置,
     * 配置文件配置如下：
     * local-first.route: '{"llm-metadata-service" : "127.0.0.1:8805"}'
     */
    @Value("#{${local-first.route:{:}}}")
    private Map<String, String> route;

    /**
     * 本地优先策略
     * 优先local-first.route 配置的映射
     * 没有再本地优先
     *
     * @param environment               环境变量
     * @param loadBalancerClientFactory 工厂
     * @param nacosDiscoveryProperties  属性
     * @return ReactorLoadBalancer
     */
    @Bean
    @ConditionalOnProperty(value = "spring.cloud.loadbalancer.nacos.enabled", havingValue = "false")
    public ReactorLoadBalancer<ServiceInstance> nacosLocalFirstLoadBalancer(Environment environment,
                                                                            LoadBalancerClientFactory loadBalancerClientFactory,
                                                                            NacosDiscoveryProperties nacosDiscoveryProperties) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        if (log.isDebugEnabled()) {
            log.debug("Use nacos local first load balancer for {} service", name);
        }
        NacosLocalFirstLoadBalancer nacosLocalFirstLoadBalancer = new NacosLocalFirstLoadBalancer(
                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
                name, nacosDiscoveryProperties);
        nacosLocalFirstLoadBalancer.setRoute(route);
        return nacosLocalFirstLoadBalancer;
    }
}

