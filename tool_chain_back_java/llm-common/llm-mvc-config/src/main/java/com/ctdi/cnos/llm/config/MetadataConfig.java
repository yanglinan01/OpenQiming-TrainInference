package com.ctdi.cnos.llm.config;


import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class MetadataConfig {

    static {
        System.setProperty("spring.cloud.nacos.discovery.metadata.startupTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date()));
    }
}
