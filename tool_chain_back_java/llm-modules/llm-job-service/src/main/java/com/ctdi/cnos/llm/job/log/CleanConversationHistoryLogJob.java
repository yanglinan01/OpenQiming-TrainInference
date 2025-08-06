package com.ctdi.cnos.llm.job.log;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.ctdi.cnos.llm.feign.intfService.DcossToolHttpClientFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/1/14 10:03
 * @Description 清理历史记录接口数据
 */
@Component("cleanConversationHistoryLogJob")
@RequiredArgsConstructor
@Slf4j
public class CleanConversationHistoryLogJob {

    private final DcossToolHttpClientFeign dcossToolHttpClientFeign;

    public void cleanHistoryData(){
        DateTime start = DateUtil.date();
        dcossToolHttpClientFeign.cleanHistoryData();
        DateTime end = DateUtil.date();
        long range = DateUtil.between(start, end, DateUnit.SECOND, true);
        log.info(DateUtil.format(start, DatePattern.NORM_DATETIME_PATTERN) + " 定时任务【清理历史记录接口数据】执行完毕！耗时为(s)：" + range);

    }
}