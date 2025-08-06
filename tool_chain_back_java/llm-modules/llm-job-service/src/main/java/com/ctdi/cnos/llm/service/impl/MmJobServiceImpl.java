package com.ctdi.cnos.llm.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.common.core.exception.job.TaskException;
import com.ctdi.cnos.llm.beans.job.MmJob;
import com.ctdi.cnos.llm.constants.JobConstants;
import com.ctdi.cnos.llm.dao.MmJobDao;
import com.ctdi.cnos.llm.service.MmJobService;
import com.ctdi.cnos.llm.utils.ScheduleUtils;
import lombok.RequiredArgsConstructor;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 定时任务调度信息 服务层
 *
 * @author huangjinhua
 * @since 2024/6/3
 */
@Service
@RequiredArgsConstructor
public class MmJobServiceImpl implements MmJobService {
    private final Scheduler scheduler;

    private final MmJobDao jobMapper;

    /**
     * 初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<MmJob> jobList = jobMapper.selectList(new LambdaQueryWrapper<>());
        for (MmJob job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    @Override
    public Page<MmJob> queryPage(Page<MmJob> page, MmJob job) {
        LambdaQueryWrapper<MmJob> queryWrapper = new LambdaQueryWrapper<>(job);
        return jobMapper.selectPage(page, queryWrapper);
    }


    @Override
    public List<MmJob> queryList(MmJob job) {
        LambdaQueryWrapper<MmJob> queryWrapper = new LambdaQueryWrapper<>(job);
        return jobMapper.selectList(queryWrapper);
    }

    @Override
    public MmJob queryById(Long jobId) {
        return jobMapper.selectById(jobId);
    }

    /**
     * 暂停任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pauseJob(MmJob job) throws SchedulerException {
        Long jobId = job.getId();
        String jobGroup = job.getJobGroup();
        job.setStatus(JobConstants.Status.PAUSE.getValue());
        int rows = jobMapper.updateById(job);
        if (rows > 0) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 恢复任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resumeJob(MmJob job) throws SchedulerException {
        Long jobId = job.getId();
        String jobGroup = job.getJobGroup();
        job.setStatus(JobConstants.Status.NORMAL.getValue());
        int rows = jobMapper.updateById(job);
        if (rows > 0) {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(MmJob job) throws SchedulerException {
        Long jobId = job.getId();
        String jobGroup = job.getJobGroup();
        int rows = jobMapper.deleteById(jobId);
        if (rows > 0) {
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 批量删除调度信息
     *
     * @param jobIds 需要删除的任务ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Long> jobIds) throws SchedulerException {
        for (Long jobId : jobIds) {
            MmJob job = jobMapper.selectById(jobId);
            this.delete(job);
        }
    }

    /**
     * 任务调度状态修改
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeStatus(MmJob job) throws SchedulerException {
        int rows = 0;
        String status = job.getStatus();
        if (JobConstants.Status.NORMAL.getValue().equals(status)) {
            rows = this.resumeJob(job);
        } else if (JobConstants.Status.PAUSE.getValue().equals(status)) {
            rows = this.pauseJob(job);
        }
        return rows;
    }

    /**
     * 立即运行任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean run(MmJob job) {
        boolean result = false;
        try {
            ScheduleUtils.runScheduleJob(scheduler, job);
            result = true;
        } catch (SchedulerException | TaskException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 新增任务
     *
     * @param job 调度信息 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertJob(MmJob job) throws SchedulerException, TaskException {
        job.setStatus(JobConstants.Status.PAUSE.getValue());
        int rows = jobMapper.insert(job);
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
        return rows;
    }

    /**
     * 更新任务的时间表达式
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateJob(MmJob job) throws SchedulerException, TaskException {
        MmJob properties = queryById(job.getId());
        job.setModifyDate(DateUtil.date());
        int rows = jobMapper.updateById(job);
        if (rows > 0) {
            this.updateSchedulerJob(job, properties.getJobGroup());
        }
        return rows;
    }

    /**
     * 更新任务
     *
     * @param job      任务对象
     * @param jobGroup 任务组名
     */
    public void updateSchedulerJob(MmJob job, String jobGroup) throws SchedulerException, TaskException {
        Long jobId = job.getId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

}