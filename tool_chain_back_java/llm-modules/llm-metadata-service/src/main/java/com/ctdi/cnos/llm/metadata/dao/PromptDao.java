/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.prompt.Prompt;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Prompt Dao
 *
 * @author huangjinhua
 * @since 2024/4/2
 */
@Mapper
public interface PromptDao extends BaseMapper<Prompt> {

    /**
     * 分页查询prompt
     *
     * @param page   分页参数
     * @param prompt 查询对象
     * @return page
     */
    Page<PromptVO> queryList(Page<PromptVO> page, @Param("prompt") PromptVO prompt);

    /**
     * 全量查询prompt
     *
     * @param prompt 查询对象
     * @return List<Prompt>
     */
    List<PromptVO> queryList(@Param("prompt") PromptVO prompt);

    /**
     * 查询prompt详情
     *
     * @param id id
     * @return Prompt
     */
    PromptVO queryById(Long id);

    /**
     * 根据用户ID统计prompt 数量
     *
     * @param dataScope 用户权限
     * @return Long
     */
    Long getPromptCount(String dataScope);

}
