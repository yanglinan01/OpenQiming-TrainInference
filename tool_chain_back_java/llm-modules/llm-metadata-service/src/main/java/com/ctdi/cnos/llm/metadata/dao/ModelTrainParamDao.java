/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctdi.cnos.llm.beans.meta.model.ModelTrainParam;
import com.ctdi.cnos.llm.beans.meta.model.ModelTrainParamVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模型超参 Dao
 *
 * @author huangjinhua
 * @since 2024/5/14
 */
@Mapper
public interface ModelTrainParamDao extends BaseMapper<ModelTrainParam> {

    /**
     * 查询列表
     *
     * @param trainParam 超参信息
     * @return int
     */
    List<ModelTrainParamVO> queryList(@Param("trainParam") ModelTrainParamVO trainParam);

    /**
     * 查询详情
     *
     * @param id 超参ID
     * @return ModelTrainParamVO
     */
    ModelTrainParamVO queryById(Long id);

    /**
     * 批量新增
     *
     * @param list 超参列表
     * @return int
     */
    int insertBatch(List<ModelTrainParam> list);

    /**
     * 根据模型训练配置ID删除超参配置
     *
     * @param modelTrainId 模型训练配置ID
     * @return int
     */
    int deleteByModelTrainId(Long modelTrainId);

    /**
     * 根据模型ID删除超参配置
     *
     * @param modelId 模型ID
     * @return int
     */
    int deleteByModelId(Long modelId);
}
