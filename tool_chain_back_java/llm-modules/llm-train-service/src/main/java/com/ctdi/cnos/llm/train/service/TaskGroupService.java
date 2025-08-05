package com.ctdi.cnos.llm.train.service;

import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.train.trainTask.TaskGroup;
import com.ctdi.cnos.llm.beans.train.trainTask.TaskGroupVO;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/24 17:10
 * @Description
 */
public interface TaskGroupService extends IBaseService<TaskGroup, TaskGroupVO> {

    /**
     * 编辑训练任务组
     *
     * @param taskGroupVO
     * @return
     */
    String edit(TaskGroupVO taskGroupVO);

    /**
     * 删除训练任务组
     *
     * @param taskGroupVO
     * @return
     */
    String delete(TaskGroupVO taskGroupVO);

    /**
     * 部署训练任务组
     *
     * @param taskGroupVO
     * @return
     */
    String deploy(TaskGroupVO taskGroupVO);

    /**
     * 取消部署训练任务组
     *
     * @param taskGroupVO
     * @return
     */
    String unDeploy(TaskGroupVO taskGroupVO);
}
