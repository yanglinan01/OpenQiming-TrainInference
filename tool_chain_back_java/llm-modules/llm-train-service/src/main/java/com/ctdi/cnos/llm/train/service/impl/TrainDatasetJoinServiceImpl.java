package com.ctdi.cnos.llm.train.service.impl;

import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSet;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainDatasetJoin;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainDatasetJoinVO;
import com.ctdi.cnos.llm.system.user.entity.User;
import com.ctdi.cnos.llm.train.dao.TrainDatasetJoinDao;
import com.ctdi.cnos.llm.train.service.TrainDatasetJoinService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/10 17:23
 * @Description
 */
@RequiredArgsConstructor
@Service("trainDatasetJoinService")
public class TrainDatasetJoinServiceImpl extends BaseService<TrainDatasetJoinDao,TrainDatasetJoin,TrainDatasetJoinVO> implements TrainDatasetJoinService {

    public static volatile Integer BATCH_SIZE=100;

    private final TrainDatasetJoinDao trainDatasetJoinDao;

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<TrainDatasetJoin> wrapper, QueryParam queryParam) {
        TrainDatasetJoin filter = queryParam.getFilterDto(TrainDatasetJoin.class);
    }


    public int insertBatch(List<TrainDatasetJoin> trainDatasetJoinList){
        Integer i = 0;
        if (CollectionUtils.isNotEmpty(trainDatasetJoinList)) {
            int pageNo = 1;
            List<TrainDatasetJoin> newlist = new ArrayList<>();
            while (true) {
                if (pageNo * BATCH_SIZE <= trainDatasetJoinList.size()) {
                    newlist = trainDatasetJoinList.subList((pageNo - 1) * BATCH_SIZE, BATCH_SIZE * pageNo);
                } else {
                    newlist = trainDatasetJoinList.subList((pageNo - 1) * BATCH_SIZE, trainDatasetJoinList.size());
                }
                if (CollectionUtils.isNotEmpty(newlist)) {
                    i = i + trainDatasetJoinDao.insertBatch(newlist);
                }
                //执行插入操作
                if (CollectionUtils.isEmpty(newlist) || newlist.size() < BATCH_SIZE) {
                    break;
                }
                pageNo++;
            }
        }
        return i;
    }

    @Override
    public List<DataSet> getDataSetByTaskId(Long taskId){
        List<DataSet> dataSetList=trainDatasetJoinDao.getDataSetByTaskId(taskId);
        return dataSetList;
    }
}
