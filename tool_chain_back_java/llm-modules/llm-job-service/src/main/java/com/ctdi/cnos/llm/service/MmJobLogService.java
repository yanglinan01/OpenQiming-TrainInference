package com.ctdi.cnos.llm.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.job.MmJobLog;

import java.util.List;

/**
 * 定时任务调度日志信息信息 服务层
 *
 * @author huangjinhua
 * @since 2024/6/3
 */
public interface MmJobLogService {
    /**
     * 分页获取quartz调度器日志的计划任务
     *
     * @param page   分页对象
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    Page<MmJobLog> queryPage(Page<MmJobLog> page, MmJobLog jobLog);

    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    List<MmJobLog> queryList(MmJobLog jobLog);

    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param id 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    MmJobLog queryById(Long id);

    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     */
    void insert(MmJobLog jobLog);

    /**
     * 批量删除调度日志信息
     *
     * @param logIds 需要删除的日志ID
     * @return 结果
     */
    int deleteByIds(List<Long> logIds);

    /**
     * 删除任务日志
     *
     * @param id 调度日志ID
     * @return 结果
     */
    int deleteById(Long id);

    /**
     * 清空任务日志
     */
    void cleanJobLog();

    /**
     * 清除x天前的任务日志
     *
     * @param days 天数
     * @return int
     */
    int cleanJobLogByDays(Integer days);
}
