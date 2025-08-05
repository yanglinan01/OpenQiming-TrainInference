package com.ctdi.cnos.llm.metadata.service;

import com.ctdi.cnos.llm.base.constant.ActionType;
import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetail;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetailVO;

import java.io.Serializable;

/**
 * 问答对测试数据集评估详情 数据操作服务接口。
 *
 * @author laiqi
 * @since 2024/09/03
 */
public interface PrTestSetEvaluationDetailService extends IBaseService<PrTestSetEvaluationDetail, PrTestSetEvaluationDetailVO> {

    /**
     * 操作
     *
     * @param id 问答对测试数据集评估详情ID
     * @param actionType 操作类型
     * @return 操作结果
     */
    boolean action(Long id, ActionType actionType);

    /**
     * 点赞
     *
     * @param id 问答对测试数据集评估详情ID
     * @return 操作结果
     */
    boolean like(Long id);

    /**
     * 点踩
     *
     * @param id 问答对测试数据集评估详情ID
     * @return 操作结果
     */
    boolean dislike(Long id);

    /**
     * 根据测试数据集评估ID删除
     * @param testSetEvaluationId 测试数据集评估ID
     * @return
     */
    boolean deleteByEvaluationId(Serializable testSetEvaluationId);

    /**
     * 判断问答对测试数据集评估反馈是否完成
     * @param testSetEvaluationId
     * @return
     */
    Boolean isComplete(Long testSetEvaluationId);
}