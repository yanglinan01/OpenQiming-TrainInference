package com.ctdi.cnos.llm.metadata.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectSpace;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectSpaceDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectSpaceVO;
import com.ctdi.cnos.llm.response.OperateResult;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 项目空间信息表 数据操作服务接口。
 *
 * @author 
 * @since 2025/06/05
 */
public interface ProjectSpaceService extends IBaseService<ProjectSpace, ProjectSpaceVO> {

    OperateResult<String> saveProjectSpace(ProjectSpaceDTO projectSpaceDTO);

    OperateResult<String> updateProjectSpaceById(ProjectSpaceDTO projectSpaceDTO);

    ProjectSpaceVO queryProjectSpaceById(@NotNull(message = "项目空间信息表ID不能为空") Long id, boolean b);

    PageResult<ProjectSpaceVO> queryProjectSpacePage(QueryParam queryParam);

    ProjectSpace selectByProjectName(LambdaQueryWrapper<ProjectSpace> projectSpaceLambdaQueryWrapper);

    List<ProjectSpaceVO> queryProjectSpaceList(QueryParam queryParam);
}
