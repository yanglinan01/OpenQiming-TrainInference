package com.ctdi.cnos.llm.metadata.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.application.ApplicationSquare;
import com.ctdi.cnos.llm.beans.meta.application.ApplicationSquareVO;

import java.util.List;

/**
 * 应用广场 service
 *
 * @author huangjinhua
 * @since 2024/6/11
 */
public interface ApplicationSquareService {

    /**
     * 分页查询应用列表
     *
     * @param page 分页对象
     * @param app  应用
     * @return 分页列表
     */
    Page<ApplicationSquareVO> queryPage(Page<ApplicationSquareVO> page, ApplicationSquareVO app);

    /**
     * 查询应用列表
     *
     * @param app 应用
     * @return List<ApplicationSquare>
     */
    List<ApplicationSquareVO> queryList(ApplicationSquareVO app);

    /**
     * 查询应用详情
     *
     * @param id 应用ID
     * @return List<ApplicationSquareVO>
     */
    ApplicationSquareVO queryById(Long id);

    /**
     * 新增应用
     *
     * @param app 应用对象
     * @return int
     */
    int insert(ApplicationSquare app);


    /**
     * 修改应用
     *
     * @param app 应用对象
     * @return int
     */
    int updateById(ApplicationSquare app);


    /**
     * 删除应用
     *
     * @param id 基应用ID
     * @return int
     */
    int deleteById(Long id);

}
