/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.controller.train;

import cn.hutool.core.map.MapUtil;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTask;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskPublishReq;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskQuery;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskVO;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTenantsResp;
import com.ctdi.cnos.llm.feign.train.TrainTaskServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 训练任务 控制层
 *
 * @author huangjinhua
 * @since 2024/5/16
 */
@Api(tags = {"训练任务操作接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/trainTask")
@Slf4j
public class TrainTaskController {


    private final TrainTaskServiceClientFeign clientFeign;


    @ApiOperation(value = "分页查询训练任务列表", notes = "分页查询训练任务列表")
    @PostMapping("/queryPage")
    public OperateResult<Map<String, Object>> queryPage(@RequestBody(required = false) TrainTaskQuery trainTaskQuery) {

        try {
            trainTaskQuery.setDeployBelong(TrainConstants.DEPLOY_BELONG_TOOL);
            List<String> types=new ArrayList<>();
            types.add(TrainConstants.MODEL_TRAIN_TYPE_LORA);
            types.add(TrainConstants.MODEL_TRAIN_TYPE_SFT);
            types.add(TrainConstants.MODEL_TRAIN_TYPE_RL_LORA);
            types.add(TrainConstants.MODEL_TRAIN_TYPE_QLORA);
            trainTaskQuery.setType(String.join(",",types));
            trainTaskQuery.setTaskOrGroup(TrainConstants.TASK_OR_GROUP_TASK);
            Map<String, Object> result = clientFeign.queryPage(trainTaskQuery);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("分页查询训练任务列表异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }

    }

    @ApiOperation(value = "分页查询意图识别训练任务列表", notes = "分页查询意图识别训练任务列表")
    @PostMapping("/queryPageByCategory")
    public OperateResult<Map<String, Object>> queryPageByCategory(@RequestBody(required = false) TrainTaskQuery trainTaskQuery) {

        try {
            trainTaskQuery.setDeployBelong(TrainConstants.DEPLOY_BELONG_TOOL);
//            trainTaskQuery.setType(TrainConstants.MODEL_TRAIN_TYPE_IR);
            Map<String, Object> result = clientFeign.queryPageByCategory(trainTaskQuery);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("分页查询训练任务列表异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }

    }

    @ApiOperation(value = "查询训练任务列表", notes = "查询训练任务列表")
    @PostMapping("/queryList")
    public OperateResult<List<TrainTaskVO>> queryList(@RequestBody(required = false) TrainTaskQuery trainTaskQuery) {
        try {
            trainTaskQuery.setDeployBelong(TrainConstants.DEPLOY_BELONG_TOOL);
            List<String> types=new ArrayList<>();
            types.add(TrainConstants.MODEL_TRAIN_TYPE_LORA);
            types.add(TrainConstants.MODEL_TRAIN_TYPE_SFT);
            types.add(TrainConstants.MODEL_TRAIN_TYPE_RL_LORA);
            types.add(TrainConstants.MODEL_TRAIN_TYPE_QLORA);
            trainTaskQuery.setType(String.join(",",types));
            List<TrainTaskVO> result = clientFeign.queryList(trainTaskQuery);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("查询训练任务列表异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }

    }

    @ApiOperation(value = "查询意图识别训练任务列表", notes = "查询意图识别训练任务列表")
    @PostMapping("/queryListByCategory")
    public OperateResult<List<TrainTaskVO>> queryListByCategory(@RequestBody(required = false) TrainTaskQuery trainTaskQuery) {
        try {
            trainTaskQuery.setDeployBelong(TrainConstants.DEPLOY_BELONG_TOOL);
//            trainTaskQuery.setType(TrainConstants.MODEL_TRAIN_TYPE_IR);
            List<TrainTaskVO> result = clientFeign.queryListByCategory(trainTaskQuery);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("查询训练任务列表异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }

    }

    @ApiOperation(value = "查询训练任务详情", notes = "查询训练任务详情")
    @GetMapping("/detail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务ID", paramType = "param")
    })
    public OperateResult<TrainTaskVO> detail(@RequestParam("id") Long id) {
        try {
            TrainTaskVO result = clientFeign.detail(id);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("查询训练任务详情异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "新增训练任务", notes = "新增训练任务")
    @PostMapping("/add")
    public OperateResult<Void> add(@RequestBody TrainTaskVO taskVO) {
        try {
            Map<String, Object> result = clientFeign.add(taskVO);
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("新增训练任务异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "迭代训练任务", notes = "迭代训练任务")
    @PostMapping("/iterate")
    public OperateResult<Void> iterate(@RequestBody TrainTaskVO taskVO) {
        try {
            Map<String, Object> result = clientFeign.iterate(taskVO);
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("迭代训练任务异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "版本启用", notes = "版本启用")
    @PostMapping("/versionEnable")
    public OperateResult<Void> versionEnable(@RequestBody TrainTaskVO taskVO) {
        try {
            Map<String, Object> result = clientFeign.versionEnable(taskVO);
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("版本启用异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "更新训练任务信息", notes = "更新训练任务信息")
    @PostMapping("/updateById")
    public OperateResult<Void> updateById(@RequestBody TrainTask task) {
        try {
            Map<String, Object> result = clientFeign.updateById(task);
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("修改训练任务异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "删除训练任务", notes = "删除训练任务")
    @DeleteMapping("/deleteById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务ID", paramType = "param")
    })
    public OperateResult<Void> deleteById(@RequestParam("id") Long id) {
        try {
            Map<String, Object> result = clientFeign.deleteById(id);
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("删除训练任务异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }


    @ApiOperation(value = "终止训练任务", notes = "终止训练任务")
    @GetMapping("/stopById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务ID", paramType = "param")
    })
    public OperateResult<Void> stopById(@RequestParam("id") Long id) {
        try {
            Map<String, Object> result = clientFeign.stopById(id);
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("终止训练任务异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "校验当前用户的训练任务数量", notes = "校验当前用户的训练任务数量")
    @GetMapping("/checkUserTrainTaskCount")
    public OperateResult<Void> checkUserTrainTaskCount() {
        try {
            if (clientFeign.checkUserTrainTaskCount()) {
                return OperateResult.success();
            }
            return OperateResult.error("当前用户训练任务数量已达上限");
        } catch (Exception e) {
            log.error("校验当前用户的训练任务数量异常", e);
            return OperateResult.error(e.getMessage());
        }
    }

    @ApiOperation(value = "模型发布", notes = "模型发布")
    @PostMapping("/publish")
    public OperateResult<Void> publish(@RequestBody TrainTaskPublishReq task) {
        try {
            Map<String, Object> result = clientFeign.publish(task);
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("修改训练任务异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    /**
     * 获取智能体项目空间
     *
     * @param authorization
     * @return
     */
    @ApiOperation(value = "获取智能体项目空间", notes = "获取智能体项目空间")
    @GetMapping("/getTenants")
    public OperateResult<List<TrainTenantsResp>> getTenants(@RequestHeader("Authorization") String authorization) {
        try {
            List<TrainTenantsResp> trainTenantsResps = clientFeign.getTenants(authorization);
            return new OperateResult<>(true, null, trainTenantsResps);
        } catch (Exception exception) {
            log.error("获取智能体项目空间", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }
}