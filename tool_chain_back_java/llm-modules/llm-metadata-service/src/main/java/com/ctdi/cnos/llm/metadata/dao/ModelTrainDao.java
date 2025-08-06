/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctdi.cnos.llm.beans.meta.model.ModelTrain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模型训练配置 Dao
 *
 * @author huangjinhua
 * @since 2024/5/14
 */
@Mapper
public interface ModelTrainDao extends BaseMapper<ModelTrain> {

    /**
     * 模型训练配置列表
     *
     * @param modelTrain 配置信息
     * @return int
     */
    List<ModelTrain> queryList(@Param("modelTrain") ModelTrain modelTrain);

    /**
     * 根据模型ID删除模型训练配置
     *
     * @param modelId 模型ID
     * @return int
     */
    int deleteByModelId(Long modelId);
}
