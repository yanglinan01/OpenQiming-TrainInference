package com.ctdi.cnos.llm.utils;

import com.ctdi.cnos.llm.beans.job.MmJob;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author huangjinhua
 * @since 2024/6/3
 */
public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, MmJob mmJob) throws Exception {
        JobInvokeUtil.invokeMethod(mmJob);
    }
}
