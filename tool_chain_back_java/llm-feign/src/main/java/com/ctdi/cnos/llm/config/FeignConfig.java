package com.ctdi.cnos.llm.config;

import com.ctdi.cnos.llm.interceptor.FeignAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public FeignAuthInterceptor authorizationRequestInterceptor() {
        return new FeignAuthInterceptor();
    }
}