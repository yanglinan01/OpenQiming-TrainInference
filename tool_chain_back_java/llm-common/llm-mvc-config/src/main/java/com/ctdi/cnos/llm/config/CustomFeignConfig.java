package com.ctdi.cnos.llm.config;

import feign.codec.Decoder;
import feign.codec.Encoder;
import okhttp3.ConnectionPool;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "spring.feign.enabled", matchIfMissing = true)
public class CustomFeignConfig {
	private okhttp3.OkHttpClient okHttpClient;
	private static ObjectFactory<HttpMessageConverters> feignObjectFactory = new FeignObjectFactory();

	@Bean
	public Decoder feignDecoder() {
		return new ResponseEntityDecoder(new SpringDecoder(feignObjectFactory));
	}

	@Bean
	public Encoder feignEncoder() {
//		return new SpringEncoder(feignObjectFactory);
		return new SpringMultipartEncoder(new SpringEncoder(feignObjectFactory));
	}

//	@Bean
//	@ConditionalOnMissingBean(Client.class)
//	public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory, SpringClientFactory clientFactory,
//                              okhttp3.OkHttpClient okHttpClient) {
//		OkHttpClient delegate = new OkHttpClient(okHttpClient);
//		return new LoadBalancerFeignClient(delegate, cachingFactory, clientFactory);
//	}

	@Bean
	@ConditionalOnMissingBean({ ConnectionPool.class })
	public ConnectionPool httpClientConnectionPool(FeignHttpClientProperties httpClientProperties,
                                                   OkHttpClientConnectionPoolFactory connectionPoolFactory) {
		Integer maxTotalConnections = httpClientProperties.getMaxConnections();
		Long timeToLive = httpClientProperties.getTimeToLive();
		TimeUnit ttlUnit = httpClientProperties.getTimeToLiveUnit();
		return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
	}

	@Bean
	public okhttp3.OkHttpClient client(OkHttpClientFactory httpClientFactory, ConnectionPool connectionPool,
                                       FeignHttpClientProperties httpClientProperties) {
		Boolean followRedirects = httpClientProperties.isFollowRedirects();
		Integer connectTimeout = httpClientProperties.getConnectionTimeout();
		Boolean disableSslValidation = httpClientProperties.isDisableSslValidation();
		this.okHttpClient = httpClientFactory.createBuilder(disableSslValidation)
				.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS).followRedirects(followRedirects)
				.connectionPool(connectionPool).build();
		return this.okHttpClient;
	}

	@PreDestroy
	public void destroy() {
		if (this.okHttpClient != null) {
			this.okHttpClient.dispatcher().executorService().shutdown();
			this.okHttpClient.connectionPool().evictAll();
		}
	}
}
