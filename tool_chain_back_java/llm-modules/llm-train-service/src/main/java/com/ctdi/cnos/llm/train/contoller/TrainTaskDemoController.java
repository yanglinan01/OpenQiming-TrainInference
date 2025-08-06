/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.train.contoller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.AiCluster;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.Strategy;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.TrainTask;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.TrainTaskVO;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.train.service.TrainTaskDemoService;
import com.ctdi.cnos.llm.util.CommonResponseUtil;
import com.ctdi.cnos.llm.util.ModelUtil;
import com.ctdi.cnos.llm.utils.DataScopeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 训练任务demo 控制层
 *
 * @author huangjinhua
 * @since 2024/9/20
 */
@Api(tags = {"训练任务接口Demo"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/trainTaskDemo")
public class TrainTaskDemoController {

    private final TrainTaskDemoService taskService;

    @ApiOperation(value = "分页查询训练任务列表", notes = "分页查询训练任务列表")
    @GetMapping("/queryPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页大小，默认为20", paramType = "param"),
            @ApiImplicitParam(name = "currentPage", value = "当前页，默认为1", paramType = "param"),
            @ApiImplicitParam(name = "name", value = "名称", paramType = "param"),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "param"),
            @ApiImplicitParam(name = "modelId", value = "模型ID", paramType = "param"),
            @ApiImplicitParam(name = "trainTarget", value = "训练位置", paramType = "param")
    })
    public Map<String, Object> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                         @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                         @RequestParam(name = "name", required = false) String name,
                                         @RequestParam(name = "status", required = false) String status,
                                         @RequestParam(name = "modelId", required = false) Long modelId,
                                         @RequestParam(name = "trainTarget", required = false) String trainTarget) {
        Page<TrainTaskVO> page = new Page<>(currentPage, pageSize);
        TrainTaskVO task = new TrainTaskVO();
        task.setName(name)
                .setStatus(status)
                .setModelId(modelId)
                .setTrainTarget(trainTarget)
                .setSelectStatus(TrainConstants.TRAIN_TASK_STATUS_WAITING)
                //只查权限允许的
                .setDataScopeSql(DataScopeUtil.dataScopeSql("a", null));

        taskService.queryList(page, task);
        Map<String, Object> result = new HashMap<>(2);
        result.put("total", page.getTotal());
        result.put("rows", page.getRecords());
        return result;
    }

    @ApiOperation(value = "查询训练任务列表", notes = "查询训练任务列表")
    @GetMapping("/queryList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", paramType = "param"),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "param"),
            @ApiImplicitParam(name = "modelId", value = "模型ID", paramType = "param"),
            @ApiImplicitParam(name = "trainTarget", value = "训练位置", paramType = "param")
    })
    public List<TrainTaskVO> queryList(@RequestParam(name = "name", required = false) String name,
                                       @RequestParam(name = "status", required = false) String status,
                                       @RequestParam(name = "modelId", required = false) Long modelId,
                                       @RequestParam(name = "trainTarget", required = false) String trainTarget) {
        TrainTaskVO task = new TrainTaskVO();
        task.setName(name)
                .setStatus(status)
                .setModelId(modelId)
                .setTrainTarget(trainTarget)
                .setSelectStatus(TrainConstants.TRAIN_TASK_STATUS_WAITING)
                //只查权限允许的
                .setDataScopeSql(DataScopeUtil.dataScopeSql("a", null));
        return taskService.queryList(task);
    }

    @ApiOperation(value = "查询AI平台集群列表", notes = "查询AI平台集群列表")
    @GetMapping("/queryAiClusterList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", paramType = "param")
    })
    public List<AiCluster> queryAiClusterList(@RequestParam(name = "name", required = false) String name) {
        AiCluster aiCluster = new AiCluster();
        aiCluster.setTitle(name);
        return taskService.queryAiClusterList(aiCluster);
    }

    @ApiOperation(value = "校验当前用户的训练任务数量", notes = "校验当前用户的训练任务数量")
    @GetMapping("/checkUserTrainTaskCount")
    public Boolean checkUserTrainTaskCount() {
        return taskService.checkUserTrainTaskCount(UserContextHolder.getUserId());
    }

    @ApiOperation(value = "查询训练任务详情", notes = "查询训练任务详情")
    @GetMapping("/detail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务ID", paramType = "param")
    })
    public TrainTaskVO detail(@RequestParam("id") Long id) {
        return taskService.queryById(id);
    }


    @ApiOperation(value = "新增训练任务", notes = "新增训练任务")
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody TrainTask task) {
        if (!taskService.checkUserTrainTaskCount(UserContextHolder.getUserId())) {
            return CommonResponseUtil.responseMap(false, "当前用户训练数量已达上限！");
        }
        if (Objects.isNull(task.getName())) {
            return CommonResponseUtil.responseMap(false, "训练任务名称不能为空！");
        }
        taskService.insert(task);
        return CommonResponseUtil.responseMap(true, "新增成功！");
    }

    @ApiOperation(value = "更新训练任务信息", notes = "更新训练任务信息")
    @PostMapping("/updateById")
    @AuthIgnore
    public Map<String, Object> updateById(@RequestBody TrainTask task) {
        if (Objects.isNull(task.getId())) {
            return CommonResponseUtil.responseMap(false, "训练任务ID不能为空！");
        }
        taskService.updateById(task);
        return CommonResponseUtil.responseMap(true, "修改成功！");
    }

    @ApiOperation(value = "删除训练任务", notes = "删除训练任务")
    @DeleteMapping("/deleteById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务ID", required = true, paramType = "param")
    })
    public Map<String, Object> deleteById(@RequestParam("id") Long id) {
        if (Objects.isNull(id)) {
            return CommonResponseUtil.responseMap(false, "训练任务ID不能为空！");
        }
        TrainTaskVO vo = taskService.queryById(id);
        if (Objects.isNull(vo)) {
            return CommonResponseUtil.responseMap(false, "找不到对应的任务！");
        }

        //训练中的不可删除
        /*List<String> notDelete = CollUtil.newArrayList(TrainConstants.TRAIN_TASK_STATUS_TRAINING);
        if (CollUtil.contains(notDelete, vo.getStatus())) {
            return CommonResponseUtil.responseMap(false, "当前状态任务不可删除！");
        }*/

        TrainTask trainTask = ModelUtil.copyTo(vo, TrainTask.class);
        taskService.delete(trainTask);
        return CommonResponseUtil.responseMap(true, "删除成功！");
    }


    @ApiOperation(value = "终止训练任务", notes = "终止训练任务")
    @GetMapping("/stopById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务ID", required = true, paramType = "param")
    })
    public Map<String, Object> stopById(@RequestParam("id") Long id) {
        if (Objects.isNull(id)) {
            return CommonResponseUtil.responseMap(false, "训练任务ID不能为空！");
        }
        TrainTaskVO vo = taskService.queryById(id);
        if (Objects.isNull(vo)) {
            return CommonResponseUtil.responseMap(false, "找不到对应的任务！");
        }
        //已完成, 失败中的不可终止
        List<String> notDelete = CollUtil.newArrayList(TrainConstants.TRAIN_TASK_STATUS_COMPLETED, TrainConstants.TRAIN_TASK_STATUS_FAILED);
        if (CollUtil.contains(notDelete, vo.getStatus())) {
            return CommonResponseUtil.responseMap(false, "当前状态任务不可终止！");
        }
        TrainTask trainTask = ModelUtil.copyTo(vo, TrainTask.class);
        taskService.stop(trainTask);
        return CommonResponseUtil.responseMap(true, "终止成功！");
    }

    @ApiOperation(value = "接口查询训练任务训练状态", notes = "接口查询训练任务训练状态")
    @GetMapping("/modifyStatusList")
    @AuthIgnore
    public String modifyStatusList() {
        TrainTask task = new TrainTask();
        int modifyStatusCount = taskService.update(task);
        return "当前有【" + modifyStatusCount + "】个任务更新任务状态";
    }

}