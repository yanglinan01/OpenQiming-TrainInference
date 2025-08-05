package com.ctdi.cnos.llm.train.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.train.trainTask.TaskGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/24 17:09
 * @Description
 */
@Mapper
public interface TaskGroupDao extends BaseDaoMapper<TaskGroup> {
}
