package com.ctdi.cnos.llm.job.log;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.ctdi.cnos.llm.feign.log.LogServiceClientFeign;
import feign.Feign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author HuangGuanSheng
 * @date 2024-07-05 11:24
 */

@Component("logModelJob")
@RequiredArgsConstructor
@Slf4j
public class LogModelJob {

    private final LogServiceClientFeign logServiceClientFeign;

    public void deleteMoreThanTimeData(){
        DateTime start = DateUtil.date();
        logServiceClientFeign.deleteMoreThanTimeData();
        DateTime end = DateUtil.date();
        long range = DateUtil.between(start, end, DateUnit.SECOND, true);
        log.info(DateUtil.format(start, DatePattern.NORM_DATETIME_PATTERN) + " 定时任务【删除超过15天日志中心-模型监控数据】执行完毕！耗时为(s)：" + range);

    }
}
