package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 问答对测试数据集评估详情 数据操作访问接口。
 *
 * @author laiqi
 * @since 2024/09/03
 */
@Mapper
public interface PrTestSetEvaluationDetailDao extends BaseDaoMapper<PrTestSetEvaluationDetail> {

    Boolean isComplete(@Param("testSetEvaluationId") Long testSetEvaluationId);
}