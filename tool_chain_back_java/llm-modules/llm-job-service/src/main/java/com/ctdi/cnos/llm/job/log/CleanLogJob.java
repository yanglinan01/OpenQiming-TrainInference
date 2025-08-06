/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.job.log;

import cn.hutool.core.date.DateUtil;
import com.ctdi.cnos.llm.service.MmJobLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 清除日志
 *
 * @author huangjinhua
 * @since 2024/6/4
 */
@Component("cleanLogJob")
@RequiredArgsConstructor
@Slf4j
public class CleanLogJob {
    private final MmJobLogService jobLogService;

    /**
     * 清除几天前的任务调度日志
     *
     * @param days 天数
     */
    public void cleanJobLog(Integer days) {
        if(days == null) {
            days = 30;
        }
        jobLogService.cleanJobLogByDays(days);
    }
}
