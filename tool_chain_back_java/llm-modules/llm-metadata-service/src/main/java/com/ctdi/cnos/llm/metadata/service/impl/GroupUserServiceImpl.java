package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.group.GroupUser;
import com.ctdi.cnos.llm.beans.meta.group.GroupUserVO;
import com.ctdi.cnos.llm.metadata.dao.GroupUserDao;
import com.ctdi.cnos.llm.metadata.service.GroupUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 用户组_用户关系 数据操作服务类
 *
 * @author wangyb
 * @since 2024/09/23
 */
@RequiredArgsConstructor
@Service("groupUserService")
public class GroupUserServiceImpl extends BaseService<GroupUserDao, GroupUser, GroupUserVO> implements GroupUserService {

    private final GroupUserDao groupUserDao;

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<GroupUser> wrapper, QueryParam queryParam) {
        GroupUser filter = queryParam.getFilterDto(GroupUser.class);
    }

    @Override
    public List<GroupUserVO> queryGroupUserIds() {
        List<GroupUserVO> list = groupUserDao.queryGroupUserIds();
        if (CollUtil.isNotEmpty(list)) {
            //"{1834434493046861826,1834434493046861827,1834434493051056129,1834434493051056130}" 转化为List<String>
            list.forEach( item -> {
                List<String> userIds = Arrays.asList(item.getUserIdsStr().replaceAll("[{}]", "").split(","));
                item.setUserIds(userIds);
            });
        }
        return list;
    }


    @Override
    public List<GroupUser> queryUserIdsByGroupId(Long groupId) {
        Assert.notBlank(groupId.toString(), "用户组ID不能为空");
        LambdaQueryWrapperX<GroupUser> wrapperX = new LambdaQueryWrapperX<GroupUser>()
                .eq(GroupUser::getGroupId, groupId)
                .orderByDesc(GroupUser::getModifyDate);
        return groupUserDao.selectList(wrapperX);
    }

}
