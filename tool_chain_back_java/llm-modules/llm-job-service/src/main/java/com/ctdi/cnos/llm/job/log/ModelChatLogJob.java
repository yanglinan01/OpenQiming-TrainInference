package com.ctdi.cnos.llm.job.log;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import com.ctdi.cnos.llm.feign.log.ModelChatLogServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 模型体验对话日志 任务。
 *
 * @author laiqi
 * @since 2024/7/17
 */
@Component("modelChatLogJob")
@RequiredArgsConstructor
@Slf4j
public class ModelChatLogJob {

    private final ModelChatLogServiceClientFeign serviceClientFeign;

    public void cleanExpiredData(Integer offsetDay) {
        DateTime start = DateUtil.date();
        offsetDay = ObjUtil.defaultIfNull(offsetDay, -15);
        OperateResult<Long> result = serviceClientFeign.deleteExpiredLog(offsetDay);
        DateTime end = DateUtil.date();
        long range = DateUtil.between(start, end, DateUnit.SECOND, true);
        log.info(DateUtil.format(start, DatePattern.NORM_DATETIME_PATTERN) + " 定时任务【删除超过" + offsetDay + "天-模型体验对话日志数据】执行完毕！删除数：" + result.getData() + "，耗时为(s)：" + range);
    }
}