package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

/**
 * 问答对测试数据集评估赞踩 数据操作访问接口。
 *
 * @author laiqi
 * @since 2024/09/04
 */
@Mapper
public interface PrTestSetEvaluationScoreDao extends BaseDaoMapper<PrTestSetEvaluationScore> {

    boolean deleteByEvaluationId(@Param("testSetEvaluationId") Serializable testSetEvaluationId);
}