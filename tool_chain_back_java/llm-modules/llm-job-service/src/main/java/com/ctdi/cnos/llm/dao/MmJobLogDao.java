package com.ctdi.cnos.llm.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctdi.cnos.llm.beans.job.MmJobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调度任务日志信息 数据层
 *
 * @author huangjinhua
 * @since 2024/6/3
 */
@Mapper
public interface MmJobLogDao extends BaseMapper<MmJobLog> {
    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     *//*
    List<MmJobLog> selectJobLogList(MmJobLog jobLog);

    *//**
     * 查询所有调度任务日志
     *
     * @return 调度任务日志列表
     *//*
    List<MmJobLog> selectJobLogAll();

    *//**
     * 通过调度任务日志ID查询调度信息
     *
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     *//*
    MmJobLog selectJobLogById(Long jobLogId);

    *//**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     * @return 结果
     *//*
    int insertJobLog(MmJobLog jobLog);

    *//**
     * 批量删除调度日志信息
     *
     * @param logIds 需要删除的数据ID
     * @return 结果
     *//*
    int deleteJobLogByIds(Long[] logIds);

    *//**
     * 删除任务日志
     *
     * @param jobId 调度日志ID
     * @return 结果
     *//*
    int deleteJobLogById(Long jobId);

    *//**
     * 清空任务日志
     *//*
    void cleanJobLog();*/

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
