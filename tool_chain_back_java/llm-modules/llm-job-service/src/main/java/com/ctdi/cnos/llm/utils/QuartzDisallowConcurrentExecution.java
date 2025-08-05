package com.ctdi.cnos.llm.utils;

import com.ctdi.cnos.llm.beans.job.MmJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（禁止并发执行）
 * 定义不能同时并发执行相同的JobDetail
 *
 * @author huangjinhua
 * @since 2024/6/3
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, MmJob mmJob) throws Exception {
        JobInvokeUtil.invokeMethod(mmJob);
    }
}
