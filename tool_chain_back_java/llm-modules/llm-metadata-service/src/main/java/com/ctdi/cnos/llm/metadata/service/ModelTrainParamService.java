package com.ctdi.cnos.llm.metadata.service;


import com.ctdi.cnos.llm.beans.meta.model.ModelTrainParam;
import com.ctdi.cnos.llm.beans.meta.model.ModelTrainParamVO;

import java.util.List;

/**
 * 模型训练超参 service
 *
 * @author huangjinhua
 * @since 2024/5/14
 */

public interface ModelTrainParamService {

    /**
     * 根据查询超参配置列表
     *
     * @param paramVO 参数对象信息
     * @return List<ModelTrainParam>
     */
    List<ModelTrainParamVO> queryList(ModelTrainParamVO paramVO);

    /**
     * 查询超参配置
     *
     * @param id 超参配置ID
     * @return List<ModelTrainParam>
     */
    ModelTrainParam queryById(Long id);

    /**
     * 新增超参配置
     *
     * @param param 超参配置对象
     * @return int
     */
    int insert(ModelTrainParam param);

    /**
     * 批量新增超参配置
     *
     * @param list 超参配置对象list
     * @return int
     */
    int insertBatch(List<ModelTrainParam> list);


    /**
     * 修改超参配置
     *
     * @param param 超参配置对象
     * @return int
     */
    int updateById(ModelTrainParam param);


    /**
     * 删除超参配置
     *
     * @param id 超参配置ID
     * @return int
     */
    int deleteById(Long id);


    /**
     * 根据模型ID删除超参配置
     *
     * @param modelId 模型ID
     * @return int
     */
    int deleteByModelId(Long modelId);
}
