package com.ctdi.cnos.llm.train.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.AiCluster;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.Strategy;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.TrainTask;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.TrainTaskVO;

import java.util.List;

/**
 * 训练任务demo service
 *
 * @author huangjinhua
 * @since 2024/9/20
 */

public interface TrainTaskDemoService {
    /**
     * 分页查询训练任务
     *
     * @param page 分页参数
     * @param task 查询对象
     * @return page
     */
    Page<TrainTaskVO> queryList(Page<TrainTaskVO> page, TrainTaskVO task);

    /**
     * 全量查询训练任务
     *
     * @param task 查询对象
     * @return List<TrainTask>
     */
    List<TrainTaskVO> queryList(TrainTaskVO task);

    /**
     * 查询AI集群列表
     *
     * @param aiCluster 查询对象
     * @return List<AiCluster>
     */
    List<AiCluster> queryAiClusterList(AiCluster aiCluster);

    /**
     * 查询训练任务
     *
     * @param id 训练任务ID
     * @return TrainTask
     */
    TrainTaskVO queryById(Long id);

    /**
     * 新增训练任务
     *
     * @param task 训练任务对象
     * @return int
     */
    int insert(TrainTask task);

    /**
     * 更新训练任务
     *
     * @param task 训练任务对象
     * @return int
     */
    int updateById(TrainTask task);

    /**
     * 更新训练任务
     *
     * @param task 训练任务对象
     * @return int
     */
    int update(TrainTask task);

    /**
     * 删除训练任务
     *
     * @param task 训练任务
     * @return int
     */
    int delete(TrainTask task);


    /**
     * 终止训练任务
     *
     * @param task 训练任务
     * @return int
     */
    int stop(TrainTask task);

    /**
     * 校验用户训练任务数量满足情况
     *
     * @param userId 用户ID
     * @return 是否满足
     */
    Boolean checkUserTrainTaskCount(Long userId);

}