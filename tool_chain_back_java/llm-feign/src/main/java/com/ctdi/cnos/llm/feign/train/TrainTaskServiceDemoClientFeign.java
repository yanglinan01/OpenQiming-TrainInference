/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.feign.train;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.AiCluster;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.TrainTask;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.TrainTaskVO;
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
 * 训练任务 OpenFeign demo
 *
 * @author huangjinhua
 * @since 2024/9/20
 */
@Component
@FeignClient(value = RemoteConstont.TRAIN_SERVICE_NAME, path = "${cnos.server.llm-train-service.contextPath}")
public interface TrainTaskServiceDemoClientFeign {


    /**
     * 分页查询训练任务列表
     *
     * @param pageSize    页大小，默认为20
     * @param currentPage 当前页，默认为1
     * @param name        名称
     * @param status      任务状态
     * @param modelId     模型ID
     * @param trainTarget 训练位置
     * @return Map<String, Object>
     */
    @GetMapping("/trainTaskDemo/queryPage")
    Map<String, Object> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                  @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                  @RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "status", required = false) String status,
                                  @RequestParam(name = "modelId", required = false) Long modelId,
                                  @RequestParam(name = "trainTarget", required = false) String trainTarget);

    /**
     * 查询训练任务列表
     *
     * @param name        名称
     * @param status      任务状态
     * @param modelId     模型ID
     * @param trainTarget 训练位置
     * @return List
     */
    @GetMapping("/trainTaskDemo/queryList")
    List<TrainTaskVO> queryList(@RequestParam(name = "name", required = false) String name,
                                @RequestParam(name = "status", required = false) String status,
                                @RequestParam(name = "modelId", required = false) Long modelId,
                                @RequestParam(name = "trainTarget", required = false) String trainTarget);

    /**
     * 查询AI平台集群列表
     *
     * @param name 集群名称
     * @return List<AiCluster>
     */
    @GetMapping("/trainTaskDemo/queryAiClusterList")
    List<AiCluster> queryAiClusterList(@RequestParam(name = "name", required = false) String name);


    /**
     * 校验用户训练任务数量满足情况
     *
     * @return 是否满足
     */
    @GetMapping("/trainTaskDemo/checkUserTrainTaskCount")
    Boolean checkUserTrainTaskCount();

    /**
     * 查询训练任务详情
     *
     * @param id 任务ID
     * @return TrainTaskVO
     */
    @GetMapping("/trainTaskDemo/detail")
    TrainTaskVO detail(@RequestParam("id") Long id);


    /**
     * 新增训练任务
     *
     * @param task 任务
     * @return Map
     */
    @PostMapping("/trainTaskDemo/add")
    Map<String, Object> add(@RequestBody TrainTask task);

    /**
     * 更新训练任务信息
     *
     * @param task 任务
     * @return Map
     */
    @PostMapping("/trainTaskDemo/updateById")
    Map<String, Object> updateById(@RequestBody TrainTask task);

    /**
     * 删除训练任务
     *
     * @param id 任务ID
     * @return Map
     */
    @DeleteMapping("/trainTaskDemo/deleteById")
    Map<String, Object> deleteById(@RequestParam("id") Long id);

    /**
     * 终止训练任务
     *
     * @param id 任务ID
     * @return Map
     */
    @GetMapping("/trainTaskDemo/stopById")
    Map<String, Object> stopById(@RequestParam("id") Long id);

    /**
     * 接口查询训练任务训练状态
     *
     * @return 任务信息
     */
    @GetMapping("/trainTaskDemo/modifyStatusList")
    String modifyStatusList();
}