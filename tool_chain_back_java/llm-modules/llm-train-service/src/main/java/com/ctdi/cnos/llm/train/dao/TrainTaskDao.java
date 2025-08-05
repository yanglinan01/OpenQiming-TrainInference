/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.train.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.operationCenter.BarCharts;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTask;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 训练任务 Dao
 *
 * @author huangjinhua
 * @since 2024/5/15
 */
@Mapper
public interface TrainTaskDao extends BaseMapper<TrainTask> {
    /**
     * 分页查询训练任务
     *
     * @param page 分页参数
     * @param task 训练任务对象
     * @return page
     */
    Page<TrainTaskVO> queryList(Page<TrainTaskVO> page, @Param("task") TrainTaskVO task);

    /**
     * 全量查询训练任务
     *
     * @param task 训练任务对象
     * @return List<Prompt>
     */
    List<TrainTaskVO> queryList(@Param("task") TrainTaskVO task);

    Page<TrainTaskVO> queryListByCategory(Page<TrainTaskVO> page, @Param("task") TrainTaskVO task);

    List<TrainTaskVO> queryListByCategory(@Param("task") TrainTaskVO task);



    /**
     * 全量查询训练任务
     *
     * @param id 训练任务id
     * @return TrainTask
     */
    TrainTaskVO queryById(Long id);

    /**
     * 根据用户ID查询每个状态的任务数量
     *
     * @param dataScopeSql 用户权限
     * @return TrainTaskVO
     */
    List<TrainTaskVO> statusCount(String dataScopeSql);

    /**
     * 根据数据集ID、任务状态查询用到的训练任务数量（不区分用户）
     *
     * @param dataSetId 数据集ID
     * @param status    任务状态
     * @return Long
     */
    Long countByDataSetId(@Param("dataSetId") Long dataSetId, @Param("status") String status);


    /**
     * 根据任务ID批量更新任务状态
     *
     * @param taskList 训练任务对象列表
     * @return int
     */
    int updateStatusBatch(@Param("taskList") List<TrainTask> taskList);


    /**
     * 统计各省模型数量
     *
     * @param month 是否按月查
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 返回各省训练模型数量
     */
    List<BarCharts> countTrainTaskGroupByProvince(@Param("month") Integer month, @Param("startTime") LocalDate startTime, @Param("endTime") LocalDate endTime );
}
