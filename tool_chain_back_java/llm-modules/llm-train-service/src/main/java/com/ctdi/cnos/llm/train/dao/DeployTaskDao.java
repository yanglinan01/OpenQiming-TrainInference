package com.ctdi.cnos.llm.train.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTask;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTaskReq;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部署任务(DeployTask)表数据库访问层
 *
 * @author wangyb
 * @since 2024-07-01 14:16:01
 */
@Mapper
public interface DeployTaskDao extends BaseMapper<DeployTask> {

    /**
     * 查询列表
     *
     * @param vo 查询条件
     * @return 列表
     */
    List<DeployTaskVO> queryList(@Param("vo") DeployTaskVO vo);

    /**
     * 查询列表
     *
     * @param page 分页对象
     * @param vo   查询条件
     * @return page
     */
    Page<DeployTaskVO> queryList(Page<DeployTaskVO> page, @Param("vo") DeployTaskVO vo);

    /**
     * 根据ID查询部署任务
     *
     * @param id
     * @return
     */
    DeployTaskVO queryById(@Param("id") Long id);

    List<DeployTaskVO> queryAllDeployedModel(@Param("vo") DeployTask deployTask);

    DeployTask queryCompletedTestDeployTask();

    void updateBatchDeployTask(@Param("ids") List<Long> ids, @Param("before") String before, @Param("after") String after);

}
