package com.ctdi.cnos.llm.metadata.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yuyong
 * @date 2024/8/15 15:36
 */
public interface PromptSequentialDetailService extends IBaseService<PromptSequentialDetail, PromptSequentialDetail> {

    void add(PromptSequentialDetail promptSequentialDetail);

    Page<PromptSequentialDetail> queryListByDataSetFileId(BigDecimal dataSetFileId, Long currentPage, Long pageSize);

    /**
     * 根据数据集id统计对应的电路数量。
     * @param dataSetId 数据集id
     * @return 数量
     */
    Long countCircuitByDataSetId(Long dataSetId);

    int insertBatch(List<PromptSequentialDetail> list, BigDecimal dataSetFileId);

    /**
     * 根据文件id删除信息
     * @param dataSetFileId
     */
    void deleteByDataSetFileId(BigDecimal dataSetFileId);
}