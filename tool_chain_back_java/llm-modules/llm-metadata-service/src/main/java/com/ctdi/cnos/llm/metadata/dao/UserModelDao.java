package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.model.UserModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户模型关系表 数据操作访问接口。
 *
 * @author wangyb
 * @since 2024/11/14
 */
@Mapper
public interface UserModelDao extends BaseDaoMapper<UserModel> {

}
