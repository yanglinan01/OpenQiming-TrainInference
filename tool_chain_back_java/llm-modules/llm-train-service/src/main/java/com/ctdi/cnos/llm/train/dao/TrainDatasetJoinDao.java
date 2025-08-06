package com.ctdi.cnos.llm.train.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSet;
import com.ctdi.cnos.llm.beans.meta.order.OrderUser;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainDatasetJoin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/10 17:11
 * @Description
 */
@Mapper
public interface TrainDatasetJoinDao extends BaseDaoMapper<TrainDatasetJoin> {

    int insertBatch(List<TrainDatasetJoin> trainDatasetJoinList);

    List<DataSet> getDataSetByTaskId(@Param("taskId") Long taskId);
}
