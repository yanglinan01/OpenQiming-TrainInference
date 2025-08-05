package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptResponseDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yuyong
 * @date 2024/8/15 10:59
 */
@Mapper
public interface PromptResponseDetailDao extends BaseDaoMapper<PromptResponseDetail> {

    int insertBatch(List<PromptResponseDetail> list);
    void addBatch(@Param("list") List<PromptResponseDetail> list);
    void deleteByDataSetFileId(@Param("dataSetFileId") BigDecimal dataSetFileId);
}