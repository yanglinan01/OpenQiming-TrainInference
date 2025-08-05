package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptCategoryDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 意图识别数据集详情(PromptCategoryDetail)表数据库访问层
 * @author wangyb
 * @since 2024-08-16 15:13:16
 */
@Mapper
public interface PromptCategoryDetailDao extends BaseDaoMapper<PromptCategoryDetail> {

    void addBatch(@Param("list") List<PromptCategoryDetail> list);
}

