package com.ctdi.cnos.llm.train.service;

import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEval;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEvalVO;

/**
 * 训练任务c-eval 评估 数据操作服务接口。
 *
 * @author huangjinhua
 * @since 2024/09/04
 */
public interface TrainTaskEvalService extends IBaseService<TrainTaskEval, TrainTaskEvalVO> {

    /**
     * 根据模型ID（训练任务ID）获取最新的评估任务；
     *
     * @param trainTaskId 训练任务ID
     * @return TrainTaskEvalVO
     */
    TrainTaskEvalVO getLastEvalByTrainTaskId(Long trainTaskId);

}
