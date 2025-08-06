/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.ctdi.cnos.llm.beans.meta.model.ModelTrain;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.ModelTrainDao;
import com.ctdi.cnos.llm.metadata.dao.ModelTrainParamDao;
import com.ctdi.cnos.llm.metadata.service.ModelTrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 模型训练超参 业务实现
 *
 * @author huangjinhua
 * @since 2024/7/3
 */

@Service
@RequiredArgsConstructor
public class ModelTrainServiceImpl implements ModelTrainService {

    private final ModelTrainDao modelTrainDao;
    private final ModelTrainParamDao modelTrainParamDao;

    @Override
    public List<ModelTrain> queryList(ModelTrain modelTrain) {
        return modelTrainDao.queryList(modelTrain);
    }

    @Override
    public ModelTrain queryById(Long id) {
        return modelTrainDao.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(ModelTrain modelTrain) {
        Long userId = UserContextHolder.getUser().getId();
        if (Objects.isNull(modelTrain.getId())) {
            modelTrain.setId(IdUtil.getSnowflakeNextId());
        }
        if (Objects.nonNull(userId)) {
            modelTrain.setCreatorId(userId);
            modelTrain.setModifierId(userId);
        }
        modelTrain.setCreateDate(DateUtil.date());
        modelTrain.setModifyDate(DateUtil.date());
        return modelTrainDao.insert(modelTrain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(ModelTrain modelTrain) {
        Long userId = UserContextHolder.getUser().getId();
        if (Objects.isNull(modelTrain.getModifierId())) {
            modelTrain.setModifierId(userId);
        }
        modelTrain.setModifyDate(DateUtil.date());
        return modelTrainDao.updateById(modelTrain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        //删除超参配置
        modelTrainParamDao.deleteByModelTrainId(id);
        return modelTrainDao.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByModelId(Long modelId) {
        //删除超参配置
        modelTrainParamDao.deleteByModelId(modelId);
        return modelTrainDao.deleteByModelId(modelId);
    }

}
