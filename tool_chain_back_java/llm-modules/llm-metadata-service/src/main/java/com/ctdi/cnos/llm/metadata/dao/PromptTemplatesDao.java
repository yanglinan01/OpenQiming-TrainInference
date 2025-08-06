package com.ctdi.cnos.llm.metadata.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptTemplates;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提示词模板表(promptTemplates)表数据库访问层
 *
 * @author wangyb
 * @since 2024-04-02 15:09:11
 */
@Mapper
public interface PromptTemplatesDao  {

    /**
     * 删除PromptTemplates对象
     * @param id id
     * @return int
     */
    int deleteById(BigDecimal id);

    
    /**
     * 新增PromptTemplates对象
     * @param promptTemplates promptTemplates对象
     * @return int
     */
    int insert(PromptTemplates promptTemplates);


    /**
     * 查询PromptTemplates详情
     * @param id id
     * @return PromptTemplates对象
     */
    PromptTemplates queryById(BigDecimal id);


    /**
     * 更新PromptTemplates对象
     * @param promptTemplates promptTemplates对象
     * @return int
     */
    int updateById(PromptTemplates promptTemplates);


    /**
     * 查询PromptTemplates列表
     * @param promptTemplates promptTemplates对象
     * @return List<PromptTemplates>
     */
    List<PromptTemplates> queryList(@Param("promptTemplates")PromptTemplates promptTemplates);


    /**
     * 分页查询PromptTemplates列表
     * @param page page参数
     * @param promptTemplates promptTemplates对象
     * @return List<PromptTemplates>
     */
    Page<PromptTemplates> queryList(Page<PromptTemplates> page, @Param("promptTemplates") PromptTemplates promptTemplates);
}

