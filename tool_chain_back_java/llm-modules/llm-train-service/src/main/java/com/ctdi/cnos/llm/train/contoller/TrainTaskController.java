/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.train.contoller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSet;
import com.ctdi.cnos.llm.beans.meta.operationCenter.BarCharts;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTaskVO;
import com.ctdi.cnos.llm.beans.train.trainTask.*;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.feign.metadata.DataSetServiceClientFeign;
import com.ctdi.cnos.llm.train.client.ApiClient;
import com.ctdi.cnos.llm.train.service.DeployTaskService;
import com.ctdi.cnos.llm.train.service.TrainTaskService;
import com.ctdi.cnos.llm.util.CommonResponseUtil;
import com.ctdi.cnos.llm.util.ModelUtil;
import com.ctdi.cnos.llm.util.StringUtils;
import com.ctdi.cnos.llm.utils.DataScopeUtil;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 训练任务 控制层
 *
 * @author huangjinhua
 * @since 2024/5/15
 */
@Api(tags = {"训练任务接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/trainTask")
@Slf4j
public class TrainTaskController {

    private final TrainTaskService taskService;

    private final DeployTaskService deployTaskService;

    private final DataSetServiceClientFeign dataSetClient;

    private final ApiClient apiClient;

    @ApiOperation(value = "分页查询训练任务列表", notes = "分页查询训练任务列表")
    @PostMapping("/queryPage")
    public Map<String, Object> queryPage(@RequestBody(required = false) TrainTaskQuery trainTaskQuery) {
        Page<TrainTaskVO> page = new Page<>(trainTaskQuery.getCurrentPage(), trainTaskQuery.getPageSize());
        TrainTaskVO task = new TrainTaskVO();
        BeanUtil.copyProperties(trainTaskQuery, task);
        task.setSelectStatus(TrainConstants.TRAIN_TASK_STATUS_WAITING)
                //只查权限允许的
                .setDataScopeSql(DataScopeUtil.dataScopeSql("a", null));
        taskService.queryList(page, task);
        Map<String, Object> result = new HashMap<>(2);
        result.put("total", page.getTotal());
        result.put("rows", page.getRecords());
        return result;
    }

    @ApiOperation(value = "分页查询意图识别训练任务列表", notes = "分页查询意图识别训练任务列表")
    @PostMapping("/queryPageByCategory")
    public Map<String, Object> queryPageByCategory(@RequestBody(required = false) TrainTaskQuery trainTaskQuery) {
        Page<TrainTaskVO> page = new Page<>(trainTaskQuery.getCurrentPage(), trainTaskQuery.getPageSize());
        TrainTaskVO task = new TrainTaskVO();
        BeanUtil.copyProperties(trainTaskQuery, task);
        task.setSelectStatus(TrainConstants.TRAIN_TASK_STATUS_WAITING)
                //只查权限允许的
                .setDataScopeSql(DataScopeUtil.dataProjectScopeSql("a", null, null, trainTaskQuery.getProjectId()));
        taskService.queryListByCategory(page, task);
        Map<String, Object> result = new HashMap<>(2);
        result.put("total", page.getTotal());
        result.put("rows", page.getRecords());
        return result;
    }

    @ApiOperation(value = "查询训练任务列表", notes = "查询训练任务列表")
    @PostMapping("/queryList")
    public List<TrainTaskVO> queryList(@RequestBody(required = false) TrainTaskQuery trainTaskQuery) {
        TrainTaskVO task = new TrainTaskVO();
        BeanUtil.copyProperties(trainTaskQuery, task);
        //只查权限允许的
        task.setDataScopeSql(DataScopeUtil.dataScopeSql("a", null));
        return taskService.queryList(task);
    }

    @ApiOperation(value = "查询意图识别训练任务列表", notes = "查询意图识别训练任务列表")
    @PostMapping("/queryListByCategory")
    public List<TrainTaskVO> queryListByCategory(@RequestBody(required = false) TrainTaskQuery trainTaskQuery) {
        TrainTaskVO task = new TrainTaskVO();
        BeanUtil.copyProperties(trainTaskQuery, task);
        //只查权限允许的
        task.setDataScopeSql(DataScopeUtil.dataProjectScopeSql("a", null, null, trainTaskQuery.getProjectId()));
        return taskService.queryList(task);
    }

    @ApiOperation(value = "查询各状态的训练任务数量，默认走权限", notes = "查询各状态的训练任务数量，默认走权限")
    @GetMapping("/statusCount")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isAll", required = false, value = "是否查全量，不做数据过滤，0是，1否", paramType = "param"),
    })
    public Map<String, Long> statusCount(@RequestParam(value = "isAll", required = false) String isAll) {
        return taskService.statusCount(isAll);
    }

    /**
     * 根据统计类型统计各省模型构建数量。
     *
     * @param type DAY：当天；MONTH：当月；ALL 累计
     * @return 统计数据
     */
    @ApiOperation(value = "根据统计类型统计各省模型构建数量", notes = "根据统计类型统计各省模型构建数量")
    @GetMapping(value = "/countTrainTaskGroupByProvince")
    public List<BarCharts> countTrainTaskGroupByProvince(@ApiParam(value = "统计类型，DAY：当天；MONTH：当月；ALL 累计", required = true, example = "DAY")
                                                         @NotNull(message = "统计类型不能为空") @RequestParam("type") StatType type) {
        return taskService.countTrainTaskGroupByProvince(type);
    }

    @ApiOperation(value = "根据数据集ID、任务状态查询绑定的训练任务数量", notes = "根据数据集ID、任务状态查询绑定的训练任务数量")
    @GetMapping("/countByDataSetId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSetId", required = true, value = "数据集ID", paramType = "param"),
            @ApiImplicitParam(name = "status", value = "任务状态", paramType = "param")
    })
    public Long countByDataSetId(@RequestParam("dataSetId") Long dataSetId, @RequestParam(value = "status", required = false) String status) {
        Long count = taskService.countByDataSetId(dataSetId, status);
        return Optional.ofNullable(count).orElse(0L);
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
    @AuthIgnore
    public TrainTaskVO detail(@RequestParam("id") Long id) {
        return taskService.queryById(id, TrainConstants.DEPLOY_BELONG_TOOL);
    }

    @ApiOperation(value = "查询训练任务VO", notes = "查询训练任务VO")
    @PostMapping("/queryVoById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务ID", paramType = "param")
    })
    @AuthIgnore
    public TrainTaskVO queryVoById(@RequestParam("id") Long id) {
        return taskService.queryById(id, TrainConstants.DEPLOY_BELONG_TOOL);
    }

    @ApiOperation(value = "查询训练任务信息", notes = "查询训练任务信息")
    @GetMapping("/getTrainTaskById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务ID", paramType = "param")
    })
    public TrainTask getTrainTaskById(@RequestParam("id") Long id) {
        return taskService.getTrainTaskById(id);
    }

    @ApiOperation(value = "接口查询训练任务训练状态", notes = "接口查询训练任务训练状态")
    @GetMapping("/modifyStatusList")
    @AuthIgnore
    public String modifyStatusList() {
        int modifyStatusCount = taskService.modifyStatusList();
        return "当前有【" + modifyStatusCount + "】个任务更新任务状态";
    }

    @ApiOperation(value = "接口训练任务发送", notes = "接口训练任务发送")
    @GetMapping("/trainTaskSend")
    @AuthIgnore
    public String trainTaskSend() {
        int sendTaskCount = taskService.trainTaskSend();
        return "当前有【" + sendTaskCount + "】个任务训练发送";
    }


    @ApiOperation(value = "新增训练任务", notes = "新增训练任务")
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody TrainTaskVO taskVO) {
//        if (!taskService.checkUserTrainTaskCount(UserContextHolder.getUserId())) {
//            return CommonResponseUtil.responseMap(false, "当前用户训练数量已达上限！");
//        }
        if (Objects.isNull(taskVO.getName())) {
            return CommonResponseUtil.responseMap(false, "训练任务名称不能为空！");
        }
        if (!taskService.checkProjectGroupCount(taskVO)) {
            return CommonResponseUtil.responseMap(false, "超过最大组任务数量3个！");
        }
        try {
            taskService.insertOrIterate(taskVO);
            return CommonResponseUtil.responseMap(true, "新增训练任务成功！");
        } catch (Exception e) {
            log.error("新增新增训练任务失败:{}", e.getMessage());
            return CommonResponseUtil.responseMap(true, "新增训练任务失败！");
        }
    }

    @ApiOperation(value = "迭代训练任务", notes = "迭代训练任务")
    @PostMapping("/iterate")
    public Map<String, Object> iterate(@RequestBody TrainTaskVO taskVO) {
//        if (!taskService.checkUserTrainTaskCount(UserContextHolder.getUserId())) {
//            return CommonResponseUtil.responseMap(false, "当前用户训练数量已达上限！");
//        }
        if (Objects.isNull(taskVO.getName())) {
            return CommonResponseUtil.responseMap(false, "训练任务名称不能为空！");
        }
        if (!taskService.checkIterateCount(taskVO)) {
            return CommonResponseUtil.responseMap(false, "超过最大迭代版本数量3个！");
        }
        try {
            //如果没有groupId，随机生成一个
            if (Objects.isNull(taskVO.getGroupId())) {
                taskVO.setGroupId(IdUtil.getSnowflakeNextId());
            }
            String result = taskService.insertOrIterate(taskVO);
            return TrainConstants.HTTP_SUCCESS_SUBMIT.equals(result)?
                    CommonResponseUtil.responseMap(true, "新增训练任务成功！"):CommonResponseUtil.responseMap(false, result);
        } catch (Exception e) {
            log.error("新增新增训练任务失败:{}", e.getMessage());
            return CommonResponseUtil.responseMap(true, "新增训练任务失败！");
        }
    }

    /**
     * 启用逻辑
     *
     * @param taskVO
     * @return
     */
    @ApiOperation(value = "版本启用", notes = "版本启用")
    @PostMapping("/versionEnable")
    public Map<String, Object> versionEnable(@RequestBody TrainTaskVO taskVO) {
        try {
            String result = deployTaskService.versionEnable(taskVO);
            return TrainConstants.HTTP_SUCCESS_SUBMIT.equals(result)?
                    CommonResponseUtil.responseMap(true, "启用训练任务成功！"):CommonResponseUtil.responseMap(false, result);
        } catch (Exception e) {
            log.error("启用训练任务失败:{}", e.getMessage());
            return CommonResponseUtil.responseMap(true, "启用训练任务失败！");
        }
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
        TrainTaskVO vo = taskService.queryById(id, TrainConstants.DEPLOY_BELONG_TOOL);
        if (Objects.isNull(vo)) {
            return CommonResponseUtil.responseMap(false, "找不到对应的任务！");
        }

        //训练中的不可删除
        List<String> notDelete = CollUtil.newArrayList(TrainConstants.TRAIN_TASK_STATUS_TRAINING);
        if (CollUtil.contains(notDelete, vo.getStatus())) {
            return CommonResponseUtil.responseMap(false, "当前状态任务不可删除！");
        }
        List<DeployTaskVO> deployTaskList = vo.getDeployTaskList();
        //已部署的任务需要先删除部署
        if (CollUtil.isNotEmpty(deployTaskList)) {
            List<DeployTaskVO> selfDeployTaskList = deployTaskList.stream().filter(o -> StringUtils.isBlank(o.getProjectSpaceId())).collect(Collectors.toList());
            List<DeployTaskVO> spaceDeployTaskList = deployTaskList.stream().filter(o -> StringUtils.isNotBlank(o.getProjectSpaceId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(selfDeployTaskList)) {
                return CommonResponseUtil.responseMap(false, "当前任务已存在部署任务，请先删除部署任务！");
            } else if (CollectionUtils.isNotEmpty(spaceDeployTaskList)) {
                return CommonResponseUtil.responseMap(false, "当前训练任务已被项目空间引用，请先卸载项目空间对应部署任务！");
            }
        }
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
        TrainTaskVO vo = taskService.queryById(id, TrainConstants.DEPLOY_BELONG_TOOL);
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

    @ApiOperation(value = "模型发布", notes = "模型发布")
    @PostMapping("/publish")
    public Map<String, Object> publish(@RequestBody TrainTaskPublishReq task, @RequestHeader("Authorization") String authorization) {
        //查询模型、数据集、基础模型信息
        TrainTaskVO param = new TrainTaskVO();
        param.setId(task.getId());
        List<TrainTaskVO> trainTaskVOS = taskService.queryList(param);
        if (trainTaskVOS.size() != 1) {
            return CommonResponseUtil.responseMap(false, "模型不存在！");
        }
        TrainTaskVO trainTask = trainTaskVOS.get(0);
        DataSet dataSet = dataSetClient.queryById(String.valueOf(trainTask.getDataSetId()));
        try {
            JSONObject data = new JSONObject();
            data.put("msg", UserContextHolder.getUserName() + "模型发布申请");
            data.put("desc", "");
            data.put("need_publish_tool", false);
            JSONObject icon = new JSONObject();
            icon.put("content", null);
            icon.put("background", null);
            JSONObject toolParam = new JSONObject();
            toolParam.put("description", task.getModelDesc());
            toolParam.put("bussiness_id", trainTask.getId().toString());
            toolParam.put("icon", icon);
            toolParam.put("parameters", new JSONArray());
            toolParam.put("labels", new JSONArray());
            toolParam.put("privacy_policy", "");
            toolParam.put("date_set", dataSet.getDataSetName());
            toolParam.put("base_model_name", trainTask.getModelName());
            if ("0".equals(task.getPublishTo())) {
                data.put("application_type", "public");
                data.put("workspace_id", null);
                toolParam.put("name", task.getModelName());
                toolParam.put("model_param", task.getModelParams());
            } else if ("1".equals(task.getPublishTo())) {
                data.put("application_type", "project");
                data.put("workspace_id", task.getProjectSpaceId());
                toolParam.put("name", trainTask.getName());
                toolParam.put("model_param", "");
            }
            data.put("tool_param", toolParam.toJSONString());
            String result = apiClient.publishModelPushAgent(data, UserContextHolder.getUserId(), authorization);
            JSONObject json = JSONObject.parseObject(result);
            if ("success".equals(json.getString("result"))) {
                return CommonResponseUtil.responseMap(true, result);
            } else {
                return CommonResponseUtil.responseMap(false, "发布失败:" + result);
            }
        } catch (Exception e) {
            return CommonResponseUtil.responseMap(false, "发布异常！");
        }

    }

    @ApiOperation(value = "获取智能体项目空间", notes = "获取智能体项目空间")
    @GetMapping("/getTenants")
    public List<TrainTenantsResp> getTenants(@RequestParam("Authorization") String authorization) {
        String result = apiClient.getTenantsTask(authorization, UserContextHolder.getUserId());
        List<TrainTenantsResp> trainTenantsRespList = new ArrayList<>();
        try {
            if (StringUtils.isNotEmpty(result)) {
                cn.hutool.json.JSONArray objects = JSONUtil.parseArray(result);
                trainTenantsRespList = JSONUtil.toList(objects, TrainTenantsResp.class);
            }
        } catch (Exception e) {
            log.error("获取智能体项目空间异常：message:" + e.getMessage() + "stackTrace:" + e.getStackTrace());
        }
        return trainTenantsRespList;
    }


}
