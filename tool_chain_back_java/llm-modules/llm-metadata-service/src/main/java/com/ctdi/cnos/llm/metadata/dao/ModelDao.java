/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.model.Model;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模型Dao
 *
 * @author huangjinhua
 * @since 2024/5/14
 */
@Mapper
public interface ModelDao extends BaseMapper<Model> {
    /**
     * 分页查询模型列表
     *
     * @param page  分页
     * @param model 模型对象
     * @return page<ModelVO>
     */
    Page<ModelVO> queryList(Page<ModelVO> page, @Param("model") ModelVO model);

    /**
     * 查询模型列表
     *
     * @param model 模型对象
     * @return page<ApplicationSquareVO>
     */
    List<ModelVO> queryList(@Param("model") ModelVO model);
    /**
     * 查询模型详情
     *
     * @param id 模型ID
     * @return page<ApplicationSquareVO>
     */
    ModelVO queryById(Long id);

}
