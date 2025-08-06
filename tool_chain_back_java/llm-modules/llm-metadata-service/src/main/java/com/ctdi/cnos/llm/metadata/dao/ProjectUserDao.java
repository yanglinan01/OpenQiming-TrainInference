package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 项目空间用户关联信息表 数据操作访问接口。
 *
 * @author 
 * @since 2025/06/05
 */
@Mapper
public interface ProjectUserDao extends BaseDaoMapper<ProjectUser> {

    void deleteByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);
}
