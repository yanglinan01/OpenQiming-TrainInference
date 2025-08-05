/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.controller.train;

import cn.hutool.core.map.MapUtil;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.AiCluster;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.TrainTask;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.TrainTaskVO;
import com.ctdi.cnos.llm.feign.train.TrainTaskServiceDemoClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 训练任务demo 控制层
 *
 * @author huangjinhua
 * @since 2024/9/20
 */
@Api(tags = {"训练任务操作接口Demo"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/trainTaskDemo")
@Slf4j
public class TrainTaskDemoController {


    private final TrainTaskServiceDemoClientFeign clientFeign;


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
    public OperateResult<Map<String, Object>> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                                        @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                                        @RequestParam(name = "name", required = false) String name,
                                                        @RequestParam(name = "status", required = false) String status,
                                                        @RequestParam(name = "modelId", required = false) Long modelId,
                                                        @RequestParam(name = "trainTarget", required = false) String trainTarget) {

        try {
            Map<String, Object> result = clientFeign.queryPage(pageSize, currentPage, name, status, modelId, trainTarget);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("分页查询训练任务列表异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }

    }

    @ApiOperation(value = "查询训练任务列表", notes = "查询训练任务列表")
    @GetMapping("/queryList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", paramType = "param"),
            @ApiImplicitParam(name = "status", value = "status", paramType = "param"),
            @ApiImplicitParam(name = "modelId", value = "模型ID", paramType = "param"),
            @ApiImplicitParam(name = "trainTarget", value = "训练位置", paramType = "param")
    })
    public OperateResult<List<TrainTaskVO>> queryList(@RequestParam(name = "name", required = false) String name,
                                                      @RequestParam(name = "status", required = false) String status,
                                                      @RequestParam(name = "modelId", required = false) Long modelId,
                                                      @RequestParam(name = "trainTarget", required = false) String trainTarget) {
        try {
            List<TrainTaskVO> result = clientFeign.queryList(name, status, modelId, trainTarget);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("查询训练任务列表异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }

    }

    @ApiOperation(value = "查询AI平台集群列表", notes = "查询AI平台集群列表")
    @GetMapping("/queryAiClusterList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", paramType = "param")
    })
    public OperateResult<List<AiCluster>> queryAiClusterList(@RequestParam(name = "name", required = false) String name) {
        try {
            List<AiCluster> result = clientFeign.queryAiClusterList(name);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("查询AI平台集群列表", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }

    }

    @ApiOperation(value = "查询AI平台集群列表", notes = "查询AI平台集群列表")
    @GetMapping("/queryStrategyList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", paramType = "param")
    })
    public OperateResult<List<AiCluster>> queryStrategyList(@RequestParam(name = "name", required = false) String name) {
        try {
            List<AiCluster> result = clientFeign.queryAiClusterList(name);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("查询AI平台集群列表", exception);
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
    public OperateResult<Void> add(@RequestBody TrainTask task) {
        try {
            Map<String, Object> result = clientFeign.add(task);
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("新增训练任务异常", exception);
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
}