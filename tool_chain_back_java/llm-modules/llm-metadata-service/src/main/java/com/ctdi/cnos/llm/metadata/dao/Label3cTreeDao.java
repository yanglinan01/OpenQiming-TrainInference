package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Label3cTree;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签树表 数据操作访问接口。
 *
 * @author 
 * @since 2025/06/10
 */
@Mapper
public interface Label3cTreeDao extends BaseDaoMapper<Label3cTree> {

    void deleteAll();
}
