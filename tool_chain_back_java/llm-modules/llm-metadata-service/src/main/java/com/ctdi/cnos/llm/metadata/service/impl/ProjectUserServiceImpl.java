package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUser;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserVO;
import com.ctdi.cnos.llm.metadata.dao.ProjectUserDao;
import com.ctdi.cnos.llm.metadata.service.ProjectUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 项目空间用户关联信息表 数据操作服务类
 *
 * @author 
 * @since 2025/06/05
 */
@RequiredArgsConstructor
@Service("projectUserService")
public class ProjectUserServiceImpl extends BaseService<ProjectUserDao, ProjectUser, ProjectUserVO> implements ProjectUserService {

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<ProjectUser> wrapper, QueryParam queryParam) {
        ProjectUser filter = queryParam.getFilterDto(ProjectUser.class);
    }
}
