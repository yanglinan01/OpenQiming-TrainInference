/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.application.ApplicationSquare;
import com.ctdi.cnos.llm.beans.meta.application.ApplicationSquareVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 应用广场DAO
 *
 * @author huangjinhua
 * @since 2024/6/11
 */
@Mapper
public interface ApplicationSquareDao extends BaseMapper<ApplicationSquare> {

    /**
     * 分页查询应用列表
     *
     * @param page 分页
     * @param app  应用对象
     * @return page<ApplicationSquareVO>
     */
    Page<ApplicationSquareVO> queryList(Page<ApplicationSquareVO> page, @Param("app") ApplicationSquareVO app);

    /**
     * 查询应用列表
     *
     * @param app 应用对象
     * @return List<ApplicationSquareVO>
     */
    List<ApplicationSquareVO> queryList(@Param("app") ApplicationSquareVO app);

    /**
     * 查询应用详情
     *
     * @param id 应用id
     * @return ApplicationSquareVO
     */
    ApplicationSquareVO queryById(Long id);


}
