package com.ctdi.cnos.llm.metadata.service.impl;

import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationScore;
import com.ctdi.cnos.llm.metadata.dao.PrTestSetEvaluationScoreDao;
import com.ctdi.cnos.llm.metadata.service.PrTestSetEvaluationScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 问答对测试数据集评估赞踩 数据操作服务类
 *
 * @author laiqi
 * @since 2024/09/04
 */
@RequiredArgsConstructor
@Service("prTestSetEvaluationScoreService")
public class PrTestSetEvaluationScoreServiceImpl extends BaseService<PrTestSetEvaluationScoreDao, PrTestSetEvaluationScore, PrTestSetEvaluationScore> implements PrTestSetEvaluationScoreService {

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<PrTestSetEvaluationScore> wrapper, QueryParam queryParam) {
        PrTestSetEvaluationScore filter = queryParam.getFilterDto(PrTestSetEvaluationScore.class);
        // 问答对测试集详情ID 查询
        wrapper.eqIfPresent(PrTestSetEvaluationScore::getEvaluationDetailId, filter.getEvaluationDetailId());
    }

    @Override
    public boolean deleteByEvaluationId(Serializable testSetEvaluationId) {
        return baseMapper.deleteByEvaluationId(testSetEvaluationId);
    }
}