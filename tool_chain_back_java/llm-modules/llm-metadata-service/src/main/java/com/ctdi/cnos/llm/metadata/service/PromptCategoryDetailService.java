package com.ctdi.cnos.llm.metadata.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptCategoryDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * 意图识别数据集详情(PromptCategoryDetail)表服务接口
 * @author wangyb
 * @since 2024-08-16 15:13:16
 */
public interface PromptCategoryDetailService{

    Page<PromptCategoryDetail> queryListByDataSetFileId(BigDecimal dataSetFileId, Long currentPage, Long pageSize);

    void addBatch(List<PromptCategoryDetail> list, BigDecimal dataSetFileId);
}

