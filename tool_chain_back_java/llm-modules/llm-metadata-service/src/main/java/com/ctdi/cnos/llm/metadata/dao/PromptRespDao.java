package com.ctdi.cnos.llm.metadata.dao;


import com.ctdi.cnos.llm.beans.meta.prompt.PromptResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 问答详情(PromptResp)表数据库访问层
 * @author wangyb
 * @since 2024-05-15 14:06:52
 */
@Mapper
public interface PromptRespDao {

    PromptResp queryByDataSetId(BigDecimal dataSetId);

    void deleteByDataSetId(@Param("dataSetId") BigDecimal dataSetId, @Param("creatorId") Long creatorId);

    PromptResp queryById(BigDecimal id);

    void add(@Param("promptResp") PromptResp promptResp);
}

