package com.ctdi.cnos.llm.metadata.service;

import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectRoleUsersDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserRole;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserRoleDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserRoleVO;
import com.ctdi.cnos.llm.response.OperateResult;

import java.util.List;

/**
 * 项目空间用户角色关联信息表 数据操作服务接口。
 *
 * @author 
 * @since 2025/06/05
 */
public interface ProjectUserRoleService extends IBaseService<ProjectUserRole, ProjectUserRoleVO> {

    PageResult<ProjectUserRoleVO> queryPagePage(long pageSize, long currentPage, Long projectId, Long roleId, String name);

    boolean deleteByEntity(ProjectUserRole projectUserRole);

    OperateResult<String> saveProjectUserRole(ProjectUserRole projectUserRole);

    OperateResult<String> updateByProjectUserRole(ProjectUserRole projectUserRole);

    Boolean judgeManager(Long projectId, Long userId);

    OperateResult<String> addBatch(ProjectRoleUsersDTO projectRoleUsersDTO);
}
