package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.group.Group;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户组 数据操作访问接口。
 *
 * @author wangyb
 * @since 2024/09/23
 */
@Mapper
public interface GroupDao extends BaseDaoMapper<Group> {

}
