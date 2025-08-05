package com.ctdi.cnos.llm.config;


import com.ctdi.cnos.llm.interceptor.AuthorizationInterceptor;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@RefreshScope
public class WebConfigInWeb extends JsonWebMvcConfig {

    @Autowired
    AuthorizationInterceptor authorizationInterceptor;

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截路径可自行配置多个 可用 ，分隔开
        String[] excludePatterns = new String[]{"/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**",
                "/api", "/api-docs", "/api-docs/**", "/doc.html/**","/web/auth/getCurrentUserInfo"};
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/**").excludePathPatterns(excludePatterns);
    }

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //super.addResourceHandlers(registry);
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/swagger/**").addResourceLocations("classpath:/swagger/");
    }

    public JsonSerializer<Object> getNullValueSerializer() {
        return NullStringSerializer.instance;
    }
}
