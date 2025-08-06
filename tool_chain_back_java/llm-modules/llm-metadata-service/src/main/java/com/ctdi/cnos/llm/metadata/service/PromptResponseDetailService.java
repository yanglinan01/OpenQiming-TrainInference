package com.ctdi.cnos.llm.metadata.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptResponseDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yuyong
 * @date 2024/8/15 15:35
 */
public interface PromptResponseDetailService {

    void add(PromptResponseDetail promptResponseDetail);

    int insertBatch(List<PromptResponseDetail> list);


    Page<PromptResponseDetail> queryListByDataSetFileId(BigDecimal dataSetFileId, Long currentPage, Long pageSize);

    void addBatch(List<PromptResponseDetail> list, BigDecimal dataSetFileId);

    void deleteByDataSetFileId(BigDecimal dataSetFileId);
}
