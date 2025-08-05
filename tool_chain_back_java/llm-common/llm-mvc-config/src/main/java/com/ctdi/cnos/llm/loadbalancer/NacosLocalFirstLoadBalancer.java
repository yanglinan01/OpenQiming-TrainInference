package com.ctdi.cnos.llm.loadbalancer;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceInstance;
import com.alibaba.cloud.nacos.balancer.NacosBalancer;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.google.common.collect.Maps;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: liub
 * @date: 2023-10-08 13:24
 * @desc: nacos 本地服务优先负载均衡器(本地>同cluster > 同group)
 */
@Setter
public class NacosLocalFirstLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private static final Logger log = LoggerFactory.getLogger(NacosLocalFirstLoadBalancer.class);

    private final String serviceId;

    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    private Set<String> localIps;

    private Map<String, String> route = new HashMap();


    public NacosLocalFirstLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId, NacosDiscoveryProperties nacosDiscoveryProperties) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
        // 使用hutool 工具获取本机IP地址
        this.localIps = NetUtil.localIpv4s();
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get().next().map(this::getInstanceResponse);
    }

    /**
     * 优先获取与本地IP一致的服务，否则获取同一集群服务
     *
     * @param serviceInstances
     * @return
     */
    private Response<ServiceInstance> getInstanceResponse(
            List<ServiceInstance> serviceInstances) {
        // serviceInstances为空且未配置指定路由,返回EmptyResponse
        if (serviceInstances.isEmpty() && !route.keySet().contains(this.serviceId)) {
            log.warn("没有可用的服务器提供服务: " + this.serviceId);
            return new EmptyResponse();
        }
        ServiceInstance instancesToChoose = null;
        // 过滤与本机IP地址一样的服务实例
        if (!serviceInstances.isEmpty() && !CollectionUtils.isEmpty(this.localIps)) {
            for (ServiceInstance instance : serviceInstances) {
                String host = instance.getHost();
                if (this.localIps.contains(host)) {
                    instancesToChoose = instance;
                }
            }
        }
        // 本地没启动服务，则取配置了指定地址的服务实例
        if (Objects.isNull(instancesToChoose) && route.keySet().contains(this.serviceId)) {
            NacosServiceInstance instance = buildNacosServiceInstance(this.serviceId);
            log.warn("Appoint for service: " + this.serviceId + ",Use url:" + instance.getUri());
            instancesToChoose = instance;
        }
        if (Objects.nonNull(instancesToChoose)) {
            log.info("负载均衡选择服务实例({}): {}:{}", this.serviceId, instancesToChoose.getHost(), instancesToChoose.getPort());
            return new DefaultResponse(instancesToChoose);
        }
        return this.getClusterInstanceResponse(serviceInstances);
    }

    /**
     * 同一集群下优先获取
     *
     * @param serviceInstances
     * @return
     */
    private Response<ServiceInstance> getClusterInstanceResponse(
            List<ServiceInstance> serviceInstances) {
        if (serviceInstances.isEmpty()) {
            log.warn("No servers available for service: " + this.serviceId);
            return new EmptyResponse();
        }

        try {
            String clusterName = this.nacosDiscoveryProperties.getClusterName();

            List<ServiceInstance> instancesToChoose = serviceInstances;
            if (StringUtils.isNotBlank(clusterName)) {
                List<ServiceInstance> sameClusterInstances = serviceInstances.stream()
                        .filter(serviceInstance -> {
                            String cluster = serviceInstance.getMetadata().get("nacos.cluster");
                            return StringUtils.equals(cluster, clusterName);
                        }).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(sameClusterInstances)) {
                    instancesToChoose = sameClusterInstances;
                }
            } else {
                log.warn(
                        "A cross-cluster call occurs，name = {}, clusterName = {}, instance = {}",
                        serviceId, clusterName, serviceInstances);
            }

            ServiceInstance instance = NacosBalancer.getHostByRandomWeight3(instancesToChoose);
            return new DefaultResponse(instance);
        } catch (Exception e) {
            log.warn("NacosLoadBalancer error", e);
            return null;
        }
    }

    private NacosServiceInstance buildNacosServiceInstance(String serviceId) {
        String url = route.get(serviceId);
        if (StrUtil.isNotBlank(url)) {
            NacosServiceInstance instance = new NacosServiceInstance();
            instance.setServiceId(this.serviceId);
            instance.setHost(url.split(":")[0]);
            instance.setPort(Integer.parseInt(url.split(":")[1]));
            instance.setSecure(false);
            instance.setMetadata(Maps.newHashMap());
            return instance;
        }
        return null;
    }
}