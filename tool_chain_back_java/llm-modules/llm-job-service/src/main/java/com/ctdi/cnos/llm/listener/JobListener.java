/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.listener;

import com.ctdi.cnos.common.core.exception.job.TaskException;
import com.ctdi.cnos.llm.config.ApplicationConfig;
import com.ctdi.cnos.llm.service.MmJobService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 初始化定时任务
 *
 * @author huangjinhua
 * @since 2024/6/3
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class JobListener implements ApplicationListener<ApplicationReadyEvent> {

    private final MmJobService mmJobService;
    private final ApplicationConfig config;
    @Override
    @SneakyThrows
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(config.isAutoStartUp()){
            log.info("正在初始化定时任务......");
            mmJobService.init();
            log.info("定时任务初始化完成！");
        }else{
            log.info("初始化定时任务已关闭，如需初始化，请修改配置job.auto-start-up");
        }
    }
}
