/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.job.train;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.ctdi.cnos.llm.feign.train.TrainTaskServiceClientFeign;
import com.ctdi.cnos.llm.feign.train.TrainTaskServiceDemoClientFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 模型训练
 *
 * @author huangjinhua
 * @since 2024/5/29
 */
@Component("trainDemoJob")
@RequiredArgsConstructor
@Slf4j
public class TrainDemoJob {


    private final TrainTaskServiceDemoClientFeign trainClient;

    /**
     * 定时查询大模型接口的训练任务状态
     */
    public void trainTaskStatus() throws Exception {
        DateTime start = DateUtil.date();
        String taskMsg = trainClient.modifyStatusList();
        DateTime end = DateUtil.date();
        long range = DateUtil.between(start, end, DateUnit.SECOND, true);
        log.info(DateUtil.format(start, DatePattern.NORM_DATETIME_PATTERN) + " 定时任务执行完毕！任务信息为：" + taskMsg + "耗时为(s)：" + range);
    }

}
