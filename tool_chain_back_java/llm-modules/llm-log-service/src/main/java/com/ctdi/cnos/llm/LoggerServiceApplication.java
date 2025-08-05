package com.ctdi.cnos.llm;

import com.ctdi.cnos.common.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@EnableRyFeignClients
public class LoggerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoggerServiceApplication.class, args);
    }
}