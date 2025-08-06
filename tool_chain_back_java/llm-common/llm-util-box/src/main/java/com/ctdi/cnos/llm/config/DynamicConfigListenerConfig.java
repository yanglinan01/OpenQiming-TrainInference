package com.ctdi.cnos.llm.config;

import com.ctdi.cnos.llm.event.DynamicConfigApplicationListener;
import com.ctdi.cnos.llm.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangyb
 * @since 2024/9/14
 */
@Configuration
public class DynamicConfigListenerConfig {

    private static final Logger log = LoggerFactory.getLogger(DynamicConfigListenerConfig.class);

    @Bean
    public DynamicConfigApplicationListener actionApplicationListener(ContextRefresher contextRefresher,
                                                                      PropertyUtil propertyUtil) {
        log.info("[DynamicConfigApplicationListener] DynamicConfigApplicationListener listener on");
        return new DynamicConfigApplicationListener(contextRefresher, propertyUtil);
    }

}
