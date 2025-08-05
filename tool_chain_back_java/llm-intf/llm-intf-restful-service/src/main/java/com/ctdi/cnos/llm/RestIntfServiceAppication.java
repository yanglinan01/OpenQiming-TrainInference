package com.ctdi.cnos.llm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@Slf4j
public class RestIntfServiceAppication {
    public static void main(String[] args) {
        SpringApplication.run(RestIntfServiceAppication.class, args);
        System.out.println("RestIntfServiceAppication 启动成功");
    }
}
