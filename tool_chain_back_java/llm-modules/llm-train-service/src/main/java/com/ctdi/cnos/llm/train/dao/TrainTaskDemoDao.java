/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.train.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.TrainTask;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.TrainTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 训练任务demo Dao
 *
 * @author huangjinhua
 * @since 2024/9/20
 */
@Mapper
public interface TrainTaskDemoDao extends BaseMapper<TrainTask> {
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


    /**
     * 全量查询训练任务
     *
     * @param id 训练任务id
     * @return TrainTask
     */
    TrainTaskVO queryById(Long id);

}
