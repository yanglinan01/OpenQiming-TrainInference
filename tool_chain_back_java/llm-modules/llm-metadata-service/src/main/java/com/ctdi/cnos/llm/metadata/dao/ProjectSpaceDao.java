package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectSpace;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目空间信息表 数据操作访问接口。
 *
 * @author 
 * @since 2025/06/05
 */
@Mapper
public interface ProjectSpaceDao extends BaseDaoMapper<ProjectSpace> {

}
