package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Type3c;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典类型表 数据操作访问接口。
 *
 * @author 
 * @since 2025/06/10
 */
@Mapper
public interface Type3cDao extends BaseDaoMapper<Type3c> {

    void deleteAll();
}
