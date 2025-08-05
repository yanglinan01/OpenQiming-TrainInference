package com.ctdi.cnos.llm.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.job.MmJobLog;
import com.ctdi.cnos.llm.dao.MmJobLogDao;
import com.ctdi.cnos.llm.service.MmJobLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务调度日志信息 服务层
 *
 * @author huangjinhua
 * @since 2024/6/3
 */
@Service
@RequiredArgsConstructor
public class MmJobLogServiceImpl implements MmJobLogService {
    private final MmJobLogDao jobLogMapper;

    @Override
    public Page<MmJobLog> queryPage(Page<MmJobLog> page, MmJobLog jobLog) {
        LambdaQueryWrapper<MmJobLog> queryWrapper = new LambdaQueryWrapper<>(jobLog);
        return jobLogMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<MmJobLog> queryList(MmJobLog jobLog) {
        LambdaQueryWrapper<MmJobLog> log = new LambdaQueryWrapper<>(jobLog);
        return jobLogMapper.selectList(log);
    }

    @Override
    public MmJobLog queryById(Long jobLogId) {
        return jobLogMapper.selectById(jobLogId);
    }

    @Override
    public void insert(MmJobLog jobLog) {
        jobLogMapper.insert(jobLog);
    }

    @Override
    public int deleteByIds(List<Long> logIds) {
        return jobLogMapper.deleteBatchIds(CollUtil.newArrayList(logIds));
    }


    @Override
    public int deleteById(Long jobId) {
        return jobLogMapper.deleteById(jobId);
    }


    @Override
    public void cleanJobLog() {
        jobLogMapper.cleanJobLog();
    }

    @Override
    public int cleanJobLogByDays(Integer days) {
        if (days == null) {
            return 0;
        }
        return jobLogMapper.cleanJobLogByDays(days);
    }
}
