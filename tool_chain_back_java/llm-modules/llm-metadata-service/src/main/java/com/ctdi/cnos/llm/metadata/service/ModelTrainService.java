package com.ctdi.cnos.llm.metadata.service;


import com.ctdi.cnos.llm.beans.meta.model.ModelTrain;

import java.util.List;

/**
 * 模型训练配置 service
 *
 * @author huangjinhua
 * @since 2024/7/3
 */

public interface ModelTrainService {

    /**
     * 根据查询模型训练配置列表
     *
     * @param modelTrain 参数对象信息
     * @return List<ModelTrain>
     */
    List<ModelTrain> queryList(ModelTrain modelTrain);

    /**
     * 查询模型训练配置
     *
     * @param id 模型训练配置ID
     * @return ModelTrain
     */
    ModelTrain queryById(Long id);

    /**
     * 新增模型训练配置
     *
     * @param modelTrain 模型训练配置对象
     * @return int
     */
    int insert(ModelTrain modelTrain);


    /**
     * 修改模型训练配置
     *
     * @param modelTrain 模型训练配置对象
     * @return int
     */
    int updateById(ModelTrain modelTrain);


    /**
     * 删除模型训练配置
     *
     * @param id 模型训练配置ID
     * @return int
     */
    int deleteById(Long id);


    /**
     * 根据模型ID删除模型训练配置
     *
     * @param modelId 模型ID
     * @return int
     */
    int deleteByModelId(Long modelId);
}
