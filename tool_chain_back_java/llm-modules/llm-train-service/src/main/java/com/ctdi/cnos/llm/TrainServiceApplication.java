package com.ctdi.cnos.llm;

import com.ctdi.cnos.common.security.annotation.EnableRyFeignClients;
import com.ctdi.cnos.llm.annotation.EnableDynamicConfigEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRyFeignClients
@EnableDynamicConfigEvent
public class TrainServiceApplication{

    public static void main(String[] args) {
        SpringApplication.run(TrainServiceApplication.class, args);
    }


}