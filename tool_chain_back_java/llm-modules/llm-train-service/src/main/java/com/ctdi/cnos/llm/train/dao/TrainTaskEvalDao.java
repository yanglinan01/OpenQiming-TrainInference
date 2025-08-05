package com.ctdi.cnos.llm.train.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEval;
import org.apache.ibatis.annotations.Mapper;

/**
 * 训练任务c-eval 评估 数据操作访问接口。
 *
 * @author huangjinhua
 * @since 2024/09/04
 */
@Mapper
public interface TrainTaskEvalDao extends BaseDaoMapper<TrainTaskEval> {

}
