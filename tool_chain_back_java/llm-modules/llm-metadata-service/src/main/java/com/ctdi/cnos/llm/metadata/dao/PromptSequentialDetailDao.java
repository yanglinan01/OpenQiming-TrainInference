package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yuyong
 * @date 2024/8/15 10:59
 */
@Mapper
public interface PromptSequentialDetailDao extends BaseDaoMapper<PromptSequentialDetail> {
    @Select("SELECT COUNT(DISTINCT circuit_id) AS distinct_circuits FROM meta.mm_prompt_sequential_detail WHERE data_set_file_id  = (select id from meta.mm_data_set_file t1 where t1.request_id=#{dataSetId})")
    Long countCircuitByDataSetId(Long dataSetId);

    int insertBatch(List<PromptSequentialDetail> list);

    void deleteByDataSetFileId(@Param("dataSetFileId") BigDecimal dataSetFileId);
}