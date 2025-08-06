package com.ctdi.cnos.llm.config;

import com.ctdi.cnos.llm.util.PropertyUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangyb
 * @since 2024/9/14
 */
@Configuration
public class DynamicConfigConfig {

    @Bean
    public PropertyUtil propertyUtil() {
        return new PropertyUtil();
    }

}
