package com.ctdi.cnos.llm.train.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTask;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTaskVO;
import com.ctdi.cnos.llm.beans.train.trainTask.TaskGroup;
import com.ctdi.cnos.llm.beans.train.trainTask.TaskGroupVO;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTask;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskVO;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.train.dao.DeployTaskDao;
import com.ctdi.cnos.llm.train.dao.TaskGroupDao;
import com.ctdi.cnos.llm.train.dao.TrainTaskDao;
import com.ctdi.cnos.llm.train.service.DeployTaskService;
import com.ctdi.cnos.llm.train.service.TaskGroupService;
import com.ctdi.cnos.llm.train.service.TrainTaskService;
import com.ctdi.cnos.llm.util.CommonResponseUtil;
import com.ctdi.cnos.llm.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/24 17:10
 * @Description
 */
@Service("taskGroupService")
@RequiredArgsConstructor
@Slf4j
public class TaskGroupServiceImpl extends BaseService<TaskGroupDao, TaskGroup, TaskGroupVO> implements TaskGroupService {

    private final TaskGroupDao taskGroupDao;

    private final TrainTaskDao trainTaskDao;

    private final DeployTaskDao deployTaskDao;

    private final TrainTaskService trainTaskService;

    private final DeployTaskService deployTaskService;

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<TaskGroup> wrapper, QueryParam queryParam) {
        TaskGroup filter = queryParam.getFilterDto(TaskGroup.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String edit(TaskGroupVO taskGroupVO) {
        //更新训练任务名称
        //查找所有训练任务
        LambdaQueryWrapper<TrainTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrainTask::getGroupId, taskGroupVO.getId());
        List<TrainTask> trainTaskList = trainTaskDao.selectList(queryWrapper);
        //更新训练任务名字
        for (TrainTask trainTask : trainTaskList) {
            trainTask.setName(taskGroupVO.getName() + "_v" + trainTask.getVersionNum());
            trainTaskDao.updateById(trainTask);
        }
        //更新任务组
        TaskGroup taskGroup = new TaskGroup();
        BeanUtil.copyProperties(taskGroupVO, taskGroup);
        taskGroup.setModifyDate(DateUtil.date());
        taskGroupDao.updateById(taskGroup);
        return TrainConstants.HTTP_SUCCESS_SUBMIT;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(TaskGroupVO taskGroupVO) {
        if (StringUtils.isNotEmpty(taskGroupVO.getDeployStatus())) {
            return "有任务部署无法删除版本！";
        }
        //删除所有训练任务
        LambdaQueryWrapper<TrainTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrainTask::getGroupId, taskGroupVO.getId());
        List<TrainTask> trainTaskList = trainTaskDao.selectList(queryWrapper);
        for (TrainTask trainTask : trainTaskList) {
            //训练中的不可删除
            List<String> notDelete = CollUtil.newArrayList(TrainConstants.TRAIN_TASK_STATUS_TRAINING);
            if (CollUtil.contains(notDelete, trainTask.getStatus())) {
                return "当前状态任务不可删除！";
            }
            trainTaskService.delete(trainTask);
        }
        //删除任务组
        taskGroupDao.deleteById(taskGroupVO.getId());
        return TrainConstants.HTTP_SUCCESS_SUBMIT;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deploy(TaskGroupVO taskGroupVO){
        LambdaQueryWrapper<TrainTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrainTask::getGroupId, taskGroupVO.getId());
        List<TrainTask> trainTaskList = trainTaskDao.selectList(queryWrapper);
        //部署启用中的任务
        List<TrainTask> enableTrainList=trainTaskList.stream().filter(o->{
            return Objects.equals(o.getVersionEnable(), SystemConstant.YES);
        }).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(enableTrainList)){
            DeployTaskVO deployTaskVO=new DeployTaskVO();
            deployTaskVO.setModelId(enableTrainList.get(0).getId());
            deployTaskVO.setDeployBelong(TrainConstants.DEPLOY_BELONG_TOOL);
            deployTaskService.save(deployTaskVO);
        }
        //更新任务组状态
        LambdaUpdateWrapper<TaskGroup> versionWrapper = new LambdaUpdateWrapper<>();
        versionWrapper.set(TaskGroup::getDeployStatus, TrainConstants.DEPLOY_TASK_STATUS_WAITING);
        versionWrapper.eq(taskGroupVO.getId() != null, TaskGroup::getId, taskGroupVO.getId());
        taskGroupDao.update(null,versionWrapper);
        return TrainConstants.HTTP_SUCCESS_SUBMIT;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String unDeploy(TaskGroupVO taskGroupVO) {
        LambdaQueryWrapper<TrainTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrainTask::getGroupId, taskGroupVO.getId());
        List<TrainTask> trainTaskList = trainTaskDao.selectList(queryWrapper);
        //部署启用中的任务
        List<TrainTask> enableTrainList=trainTaskList.stream().filter(o->{
            return Objects.equals(o.getVersionEnable(), SystemConstant.YES);
        }).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(enableTrainList)){
            LambdaQueryWrapper<DeployTask> deployWrapper = new LambdaQueryWrapper<>();
            deployWrapper.eq(DeployTask::getModelId, enableTrainList.get(0).getId());
            List<DeployTask> deployTaskList=deployTaskDao.selectList(deployWrapper);
            if(CollUtil.isNotEmpty(enableTrainList)){
                DeployTask deployTask=deployTaskList.get(0);
                if (SystemConstant.YES.equals(deployTask.getAgentStatus())) {
                    return "当前模型已被智能体引用, 无法被删除, 请先解除绑定";
                }
                if (SystemConstant.YES.equals(deployTask.getRegisterStatus())) {
                    return "当前模型已被注册到DCOOS, 无法被删除, 请先下线";
                }
                List<String> status = CollUtil.newArrayList(TrainConstants.DEPLOY_TASK_STATUS_DEPLOYING);
                if (StrUtil.isNotBlank(deployTask.getStatus()) && status.contains(deployTask.getStatus())) {
                    return "正在部署中的任务无法被删除";
                }
                boolean flag=deployTaskService.deleteById(deployTask.getId());
                if ( flag){
                    //更新任务组部署状态
                    LambdaUpdateWrapper<TaskGroup> versionWrapper = new LambdaUpdateWrapper<>();
                    versionWrapper.set(TaskGroup::getDeployStatus, "");
                    versionWrapper.eq(taskGroupVO.getId() != null, TaskGroup::getId, taskGroupVO.getId());
                    taskGroupDao.update(null,versionWrapper);
                    return TrainConstants.HTTP_SUCCESS_SUBMIT;
                }else{
                    return "取消部署失败！";
                }
            }
        }
        return "未找到部署任务！";
    }
}
