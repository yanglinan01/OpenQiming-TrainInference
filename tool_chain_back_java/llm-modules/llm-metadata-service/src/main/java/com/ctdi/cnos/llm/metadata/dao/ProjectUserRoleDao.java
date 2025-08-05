package com.ctdi.cnos.llm.metadata.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目空间用户角色关联信息表 数据操作访问接口。
 *
 * @author 
 * @since 2025/06/05
 */
@Mapper
public interface ProjectUserRoleDao extends BaseDaoMapper<ProjectUserRole> {

    void updateByEntity(@Param("entity") ProjectUserRole projectUserRole);

    Page<ProjectUserRole> selectByCondition( @Param("projectId")Long projectId,
                                             @Param("roleId")Long roleId,
                                             @Param("userIds")List<Long> userIds,
                                             Page<ProjectUserRole> page);
}
