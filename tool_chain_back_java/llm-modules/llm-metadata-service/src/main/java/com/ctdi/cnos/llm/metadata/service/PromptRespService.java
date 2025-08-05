package com.ctdi.cnos.llm.metadata.service;


import com.ctdi.cnos.llm.beans.meta.prompt.PromptResp;

import java.math.BigDecimal;

/**
 * 问答详情(PromptResp)表服务接口
 * @author wangyb
 * @since 2024-05-15 14:06:52
 */
public interface PromptRespService{

    /**
     * 查询问答对根据数据集ID
     * @param dataSetId dataSetId
     * @return 查询问答对详情
     */
    PromptResp queryByDataSetId(BigDecimal dataSetId);

    /**
     * 删除问答对根据数据集ID
     * @param dataSetId dataSetId
     */
    void deleteByDataSetId(BigDecimal dataSetId);

    /**
     * 新增问答对
     * @param savePath savePath
     * @param dataSetId dataSetId
     * @param templateType templateType
     * @param currentId currentId
     */
    void addPromptResp(String savePath, BigDecimal dataSetId, String templateType, BigDecimal currentId, String setType, String dataType);
}

