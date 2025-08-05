package com.ctdi.cnos.llm.metadata.service;

import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.group.GroupUser;
import com.ctdi.cnos.llm.beans.meta.group.GroupUserVO;

import java.util.List;

/**
 * 用户组_用户关系 数据操作服务接口。
 *
 * @author wangyb
 * @since 2024/09/23
 */
public interface GroupUserService extends IBaseService<GroupUser, GroupUserVO> {

    /**
     * 查询用户组中的用户列表
     * @return 用户组中的用户列表
     */
    List<GroupUserVO> queryGroupUserIds();

    /**
     * 根据用户组ID查找用户列表
     * @param groupId 用户组ID
     * @return 用户列表
     */
    List<GroupUser> queryUserIdsByGroupId(Long groupId);
}
