package com.ctdi.cnos.llm.controller.train;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTask;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTaskVO;
import com.ctdi.cnos.llm.beans.train.trainTask.TaskGroupVO;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTask;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskVO;
import com.ctdi.cnos.llm.constant.Constants;
import com.ctdi.cnos.llm.feign.train.DeployTaskServiceClientFeign;
import com.ctdi.cnos.llm.feign.train.TrainTaskServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/27 11:13
 * @Description
 */
@Api(tags = {"任务组操作接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/taskGroup")
@Slf4j
public class TaskGroupController {
    private final TrainTaskServiceClientFeign clientFeign;

    private final DeployTaskServiceClientFeign deployTaskServiceClientFeign;

    @ApiOperation(value = "任务组编辑", notes = "任务组编辑")
    @PostMapping("/edit")
    public OperateResult<Void> taskGroupEdit(@RequestBody TaskGroupVO taskGroupVO) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            if(TrainConstants.TASK_OR_GROUP_TASK.equals(taskGroupVO.getTaskOrGroup())){
                TrainTask trainTask=new TrainTask();
                BeanUtil.copyProperties(taskGroupVO,trainTask);
                result = clientFeign.updateById(trainTask);
            }
            if(TrainConstants.TASK_OR_GROUP_GROUP.equals(taskGroupVO.getTaskOrGroup())){
                result = clientFeign.taskGroupEdit(taskGroupVO);
            }
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("任务组编辑异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "任务组删除", notes = "任务组删除")
    @PostMapping("/delete")
    public OperateResult<Void> taskGroupDelete(@RequestBody TaskGroupVO taskGroupVO) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            if(TrainConstants.TASK_OR_GROUP_TASK.equals(taskGroupVO.getTaskOrGroup())){
                result = clientFeign.deleteById(taskGroupVO.getId());
            }
            if(TrainConstants.TASK_OR_GROUP_GROUP.equals(taskGroupVO.getTaskOrGroup())){
                result = clientFeign.taskGroupDelete(taskGroupVO);
            }
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("任务组删除异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "部署任务组", notes = "部署任务组")
    @PostMapping("/deploy")
    public OperateResult<Void> taskGroupDeploy(@RequestBody TaskGroupVO taskGroupVO) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            if(TrainConstants.TASK_OR_GROUP_TASK.equals(taskGroupVO.getTaskOrGroup())){
                DeployTaskVO deployTask=new DeployTaskVO();
                deployTask.setDeployType(Constants.DEPLOY_TYPE_TRAIN);
                deployTask.setDeployBelong(TrainConstants.DEPLOY_BELONG_TOOL);
                deployTask.setModelId(taskGroupVO.getId());
                deployTaskServiceClientFeign.add(deployTask);
            }
            if(TrainConstants.TASK_OR_GROUP_GROUP.equals(taskGroupVO.getTaskOrGroup())){
                result = clientFeign.taskGroupDeploy(taskGroupVO);
            }

            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("部署任务组异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "取消部署任务组", notes = "部署任务组")
    @PostMapping("/unDeploy")
    public OperateResult<Void> taskGroupUnDeploy(@RequestBody TaskGroupVO taskGroupVO) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            if(TrainConstants.TASK_OR_GROUP_TASK.equals(taskGroupVO.getTaskOrGroup())){
                deployTaskServiceClientFeign.deleteById(taskGroupVO.getDeployTaskId());
            }
            if(TrainConstants.TASK_OR_GROUP_GROUP.equals(taskGroupVO.getTaskOrGroup())){
                result = clientFeign.taskGroupUnDeploy(taskGroupVO);
            }
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("取消部署任务组异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

}
