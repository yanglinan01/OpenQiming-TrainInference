package com.ctdi.cnos.llm.train.service;

import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSet;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainDatasetJoin;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainDatasetJoinVO;

import java.util.List;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/10 17:22
 * @Description 训练数据集关系表
 */
public interface TrainDatasetJoinService extends IBaseService<TrainDatasetJoin,TrainDatasetJoinVO>{

    /**
     * 批量新增
     * @param trainDatasetJoinList
     * @return
     */
    int insertBatch(List<TrainDatasetJoin> trainDatasetJoinList);

    /**
     *
     * @param taskId
     * @return
     */
    List<DataSet> getDataSetByTaskId(Long taskId);
}
