package com.ctdi.cnos.llm;

import com.ctdi.cnos.common.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 定时任务调度
 * @author huangjinhua
 * @since 20245/29
 */
@SpringBootApplication
@EnableRyFeignClients
public class JobApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }

}
