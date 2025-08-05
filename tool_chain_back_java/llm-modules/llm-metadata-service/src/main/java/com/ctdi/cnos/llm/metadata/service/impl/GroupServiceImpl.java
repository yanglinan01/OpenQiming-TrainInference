package com.ctdi.cnos.llm.metadata.service.impl;

import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.group.Group;
import com.ctdi.cnos.llm.beans.meta.group.GroupVO;
import com.ctdi.cnos.llm.metadata.dao.GroupDao;
import com.ctdi.cnos.llm.metadata.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户组 数据操作服务类
 *
 * @author wangyb
 * @since 2024/09/23
 */
@RequiredArgsConstructor
@Service("groupService")
public class GroupServiceImpl extends BaseService<GroupDao, Group, GroupVO> implements GroupService {

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<Group> wrapper, QueryParam queryParam) {
        Group filter = queryParam.getFilterDto(Group.class);
    }	
}
