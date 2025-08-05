package com.ctdi.cnos.llm.job.log;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.ctdi.cnos.llm.feign.log.SessionCacheServiceClientFegin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("cleanSessionCacheDataJob")
@RequiredArgsConstructor
@Slf4j
public class cleanSessionCacheDataJob {
    private final SessionCacheServiceClientFegin sessionCacheServiceClientFegin;
    public void cleanSessionCacheData() {
        DateTime start = DateUtil.date();
        int count=sessionCacheServiceClientFegin.cleanHistoryData();
        DateTime end = DateUtil.date();
        long range = DateUtil.between(start, end, DateUnit.SECOND, true);
        log.info(DateUtil.format(start, DatePattern.NORM_DATETIME_PATTERN) + " 定时任务【cleanSessionCacheData清理会话缓存接口数据】执行完毕！耗时为(s)：" + range+"，共"+count+"条数据");

    }
}
