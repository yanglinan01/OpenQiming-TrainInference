package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluation;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问答对测试数据集评估 数据操作访问接口。
 *
 * @author laiqi
 * @since 2024/09/03
 */
@Mapper
public interface PrTestSetEvaluationDao extends BaseDaoMapper<PrTestSetEvaluation> {

    List<PrTestSetEvaluationVO> queryByDeployTaskId(@Param("id") Long id);

    List<PrTestSetEvaluation> queryPrTestSetEvaluationListBySendStatus(@Param("sendStatus") String sendStatus, @Param("limit") Integer limit);
}