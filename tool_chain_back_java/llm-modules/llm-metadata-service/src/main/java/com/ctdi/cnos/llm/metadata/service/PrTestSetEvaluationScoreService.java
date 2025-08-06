package com.ctdi.cnos.llm.metadata.service;

import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationScore;

import java.io.Serializable;

/**
 * 问答对测试数据集评估赞踩 数据操作服务接口。
 *
 * @author laiqi
 * @since 2024/09/04
 */
public interface PrTestSetEvaluationScoreService extends IBaseService<PrTestSetEvaluationScore, PrTestSetEvaluationScore> {

    boolean deleteByEvaluationId(Serializable testSetEvaluationId);
}