/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.feign.train;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.beans.meta.operationCenter.BarCharts;
import com.ctdi.cnos.llm.beans.train.trainTask.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 训练任务 OpenFeign
 *
 * @author huangjinhua
 * @since 2024/5/16
 */
@Component
@FeignClient(value = RemoteConstont.TRAIN_SERVICE_NAME, path = "${cnos.server.llm-train-service.contextPath}")
public interface TrainTaskServiceClientFeign {


    /**
     * 分页查询训练任务列表
     *
     * @param trainTaskQuery 训练任务查询参数
     * @return Map<String, Object>
     */
    @PostMapping("/trainTask/queryPage")
    Map<String, Object> queryPage(@RequestBody(required = false) TrainTaskQuery trainTaskQuery);

    /**
     * 分页查询意图识别训练任务列表
     *
     * @param trainTaskQuery 训练任务查询参数
     * @return Map<String, Object>
     */
    @PostMapping("/trainTask/queryPageByCategory")
    Map<String, Object> queryPageByCategory(@RequestBody(required = false) TrainTaskQuery trainTaskQuery);

    /**
     * 查询训练任务列表
     *
     * @param trainTaskQuery 训练任务查询参数
     * @return List
     */
    @PostMapping("/trainTask/queryList")
    List<TrainTaskVO> queryList(@RequestBody(required = false) TrainTaskQuery trainTaskQuery);

    /**
     * 查询训练任务列表
     *
     * @param trainTaskQuery 训练任务查询参数
     * @return List
     */
    @PostMapping("/trainTask/queryListByCategory")
    List<TrainTaskVO> queryListByCategory(@RequestBody(required = false) TrainTaskQuery trainTaskQuery);

    /**
     * 查询各状态的训练任务数量，默认走权限
     *
     * @param isAll 是否查全量，不做数据过滤，0是，1否
     * @return Map
     */
    @GetMapping("/trainTask/statusCount")
    Map<String, Long> statusCount(@RequestParam(value = "isAll", required = false) String isAll);

    /**
     * 根据数据集ID查询绑定的训练任务数量
     *
     * @param dataSetId 数据集ID
     * @param status    任务状态（可空）
     * @return Long
     */
    @GetMapping("/trainTask/countByDataSetId")
    Long countByDataSetId(@RequestParam("dataSetId") Long dataSetId, @RequestParam(value = "status", required = false) String status);

    /**
     * 校验用户训练任务数量满足情况
     *
     * @return 是否满足
     */
    @GetMapping("/trainTask/checkUserTrainTaskCount")
    Boolean checkUserTrainTaskCount();

    /**
     * 查询训练任务详情
     *
     * @param id 任务ID
     * @return TrainTaskVO
     */
    @GetMapping("/trainTask/detail")
    TrainTaskVO detail(@RequestParam("id") Long id);

    @PostMapping("/trainTask/queryVoById")
    TrainTaskVO queryVoById(@RequestParam("id") Long id);

    /**
     * 查询训练任务详情
     *
     * @param id 任务ID
     * @return TrainTaskVO
     */
    @GetMapping("/trainTask/getTrainTaskById")
    TrainTask getTrainTaskById(@RequestParam("id") Long id);


    /**
     * 接口查询训练任务训练状态
     *
     * @return 任务信息
     */
    @GetMapping("/trainTask/modifyStatusList")
    String modifyStatusList();

    /**
     * 接口查询训练任务训练状态
     *
     * @return 任务信息
     */
    @GetMapping("/trainTask/trainTaskSend")
    String trainTaskSend();


    /**
     * 新增训练任务
     *
     * @param taskVO 任务
     * @return Map
     */
    @PostMapping("/trainTask/add")
    Map<String, Object> add(@RequestBody TrainTaskVO taskVO);

    /**
     * 迭代训练任务
     *
     * @param taskVO 任务
     * @return Map
     */
    @PostMapping("/trainTask/iterate")
    Map<String, Object> iterate(@RequestBody TrainTaskVO taskVO);

    /**
     * 版本启用
     * @param taskVO
     * @return
     */
    @PostMapping("/trainTask/versionEnable")
    Map<String, Object> versionEnable(@RequestBody TrainTaskVO taskVO);

    /**
     * 更新训练任务信息
     *
     * @param task 任务
     * @return Map
     */
    @PostMapping("/trainTask/updateById")
    Map<String, Object> updateById(@RequestBody TrainTask task);

    /**
     * 删除训练任务
     *
     * @param id 任务ID
     * @return Map
     */
    @DeleteMapping("/trainTask/deleteById")
    Map<String, Object> deleteById(@RequestParam("id") Long id);

    /**
     * 终止训练任务
     *
     * @param id 任务ID
     * @return Map
     */
    @GetMapping("/trainTask/stopById")
    Map<String, Object> stopById(@RequestParam("id") Long id);


    /**
     * 根据统计类型统计省份维度数据画图。
     *
     * @param type DAY：当天；MONTH：当月；ALL 累计
     * @return 统计各省模型构建数量
     */
    @GetMapping(value = "/trainTask/countTrainTaskGroupByProvince")
    List<BarCharts> countTrainTaskGroupByProvince(@RequestParam("type") StatType type);

    /**
     * 模型发布
     * @param task
     * @return
     */
    @PostMapping(value = "/trainTask/publish")
    Map<String, Object> publish(@RequestBody TrainTaskPublishReq task);

    /**
     * 获取智能体项目空间
     * @param authorization
     * @return
     */
    @GetMapping("/trainTask/getTenants")
    List<TrainTenantsResp> getTenants(@RequestParam("Authorization") String authorization);

    /**
     * 任务组编辑
     * @param taskGroupVO
     * @return
     */
    @PostMapping("/taskGroup/edit")
    Map<String, Object> taskGroupEdit(@RequestBody TaskGroupVO taskGroupVO);

    /**
     * 删除任务组
     * @param taskGroupVO
     * @return
     */
    @PostMapping("/taskGroup/delete")
    Map<String, Object> taskGroupDelete(@RequestBody TaskGroupVO taskGroupVO);

    /**
     * 部署任务组
     * @param taskGroupVO
     * @return
     */
    @PostMapping("/taskGroup/deploy")
    Map<String, Object> taskGroupDeploy(@RequestBody TaskGroupVO taskGroupVO);

    /**
     * 取消部署任务组
     * @param taskGroupVO
     * @return
     */
    @PostMapping("/taskGroup/unDeploy")
    Map<String, Object> taskGroupUnDeploy(@RequestBody TaskGroupVO taskGroupVO);
}