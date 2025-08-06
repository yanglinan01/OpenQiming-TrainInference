package com.ctdi.cnos.llm.metadata.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.model.Model;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;

import java.util.List;

/**
 * 基础模型 service
 *
 * @author huangjinhua
 * @since 2024/5/14
 */

public interface ModelService {


    /**
     * 查询列表
     *
     * @param page    分页信息
     * @param modelVO 模型对象信息
     * @return Page<ModelVO>
     */
    Page<ModelVO> queryPage(Page<ModelVO> page, ModelVO modelVO);

    /**
     * 查询列表
     *
     * @param modelVO 模型对象信息
     * @return List<ModelVO>
     */
    List<ModelVO> queryList(ModelVO modelVO);

    /**
     * 查询模型
     *
     * @param id 模型ID
     * @return List<ModelVO>
     */
    ModelVO queryById(Long id);

    /**
     * 新增模型
     *
     * @param model 模型对象
     * @return int
     */
    int insert(Model model);


    /**
     * 修改模型
     *
     * @param model 模型对象
     * @return int
     */
    int updateById(Model model);


    /**
     * 删除模型
     *
     * @param id 模型ID
     * @return int
     */
    int deleteById(Long id);

}
