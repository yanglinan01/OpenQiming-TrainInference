/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.controller.common;

import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.feign.metadata.PromptServiceClientFeign;
import com.ctdi.cnos.llm.feign.train.TrainTaskServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 统计类 控制接口
 *
 * @author huangjinhua
 * @since 2024/5/16
 */
@Api(tags = {"统计类接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/statistics")
@Slf4j
public class StatisticsController {

    private final PromptServiceClientFeign promptClient;

    private final TrainTaskServiceClientFeign trainTaskClient;

    @ApiOperation(value = "根据当前用户获取可用模型、prompt模版、模型训练任务、可用插件数量", notes = "根据当前用户获取可用模型、prompt模版、模型训练任务、可用插件数量")
    @GetMapping("/getMyAbilitiesByUserId")
    public OperateResult<Map<String, Long>> getPromptCountByUserId() {
        Map<String, Long> result = new HashMap<>(4);
        try {
            //prompt模版
            result.put("prompt", Optional.ofNullable(promptClient.getPromptCountByUserId()).orElse(0L));
            Map<String, Long> taskMap = trainTaskClient.statusCount(SystemConstant.NO);
            //可用模型
            //result.put("availableModel", Optional.ofNullable(taskMap).map(map -> map.get("completed")).orElse(0L));
            result.put("availableModel", 0L);
            //模型训练任务(所有状态)
            result.put("trainTask", Optional.ofNullable(taskMap).map(map -> map.values().stream().mapToLong(Long::longValue).sum()).orElse(0L));
            //result.put("trainTask", 0L);
            //可用插件数量
            result.put("availablePlugin", 0L);

            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("根据当前用户获取我的能力数据异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }
}