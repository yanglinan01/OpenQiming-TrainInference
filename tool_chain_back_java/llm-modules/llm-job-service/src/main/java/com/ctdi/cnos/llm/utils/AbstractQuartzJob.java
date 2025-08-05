package com.ctdi.cnos.llm.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import com.ctdi.cnos.common.core.constant.ScheduleConstants;
import com.ctdi.cnos.common.core.utils.ExceptionUtil;
import com.ctdi.cnos.common.core.utils.SpringUtils;
import com.ctdi.cnos.common.core.utils.bean.BeanUtils;
import com.ctdi.cnos.llm.beans.job.MmJob;
import com.ctdi.cnos.llm.beans.job.MmJobLog;
import com.ctdi.cnos.llm.service.MmJobLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 抽象quartz调用
 *
 * @author huangjinhua
 * @since 2024/6/3
 */
public abstract class AbstractQuartzJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        MmJob mmJob = new MmJob();
        BeanUtils.copyBeanProp(mmJob, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        try {
            before(context, mmJob);
            doExecute(context, mmJob);
            after(context, mmJob, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(context, mmJob, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param mmJob   系统计划任务
     */
    protected void before(JobExecutionContext context, MmJob mmJob) {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     *
     * @param context  工作执行上下文对象
     * @param mmJob 系统计划任务
     */
    protected void after(JobExecutionContext context, MmJob mmJob, Exception e) {
        Date startTime = threadLocal.get();
        threadLocal.remove();

        MmJobLog mmJobLog = new MmJobLog();
        mmJobLog.setId(IdUtil.getSnowflakeNextId());
        mmJobLog.setJobId(mmJob.getId());
        mmJobLog.setJobName(mmJob.getJobName());
        mmJobLog.setJobGroup(mmJob.getJobGroup());
        mmJobLog.setInvokeTarget(mmJob.getInvokeTarget());
        mmJobLog.setCreateDate(DateUtil.date());
        long runMs = DateUtil.betweenMs(startTime, DateUtil.date());
        mmJobLog.setJobMessage(mmJobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
        if (e != null) {
            mmJobLog.setStatus("1");
            String errorMsg = CharSequenceUtil.sub(ExceptionUtil.getExceptionMessage(e), 0, 2000);
            mmJobLog.setExceptionInfo(errorMsg);
        } else {
            mmJobLog.setStatus("0");
        }

        // 写入数据库当中
        SpringUtils.getBean(MmJobLogService.class).insert(mmJobLog);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param mmJob   系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, MmJob mmJob) throws Exception;
}
