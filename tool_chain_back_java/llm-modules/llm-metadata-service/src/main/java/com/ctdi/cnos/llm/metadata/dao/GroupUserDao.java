package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.group.GroupUser;
import com.ctdi.cnos.llm.beans.meta.group.GroupUserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户组_用户关系 数据操作访问接口。
 *
 * @author wangyb
 * @since 2024/09/23
 */
@Mapper
public interface GroupUserDao extends BaseDaoMapper<GroupUser> {

    List<GroupUserVO> queryGroupUserIds();

}
