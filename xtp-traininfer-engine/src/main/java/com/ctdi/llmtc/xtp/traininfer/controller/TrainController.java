package com.ctdi.llmtc.xtp.traininfer.controller;

import com.ctdi.llmtc.xtp.traininfer.beans.req.TrainReq;
import com.ctdi.llmtc.xtp.traininfer.service.TrainService;
import com.ctdi.llmtc.xtp.traininfer.util.JSONUtil;
import com.ctdi.llmtc.xtp.traininfer.util.OperateResult;
import com.ctdi.llmtc.xtp.traininfer.util.validator.Groups;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 训练对外接口控制器类。
 *
 * @author ctdi
 * @since 2025/5/28
 */
@RestController
@Slf4j
public class TrainController {

    @Autowired
    private TrainService trainService;

    @PostMapping("/train/submit")
    public OperateResult<String> trainSubmit(@RequestBody TrainReq trainReq) {
        log.info("[{}] TrainController train submit request. [{}]", trainReq.getTaskId(), JSONUtil.toJSONString(trainReq));
        OperateResult<String> result = trainService.trainSubmit(trainReq);
        log.info("[{}] TrainController train submit response. [{}]", trainReq.getTaskId(), JSONUtil.toJSONString(result));
        return result;
    }

    @GetMapping("/train/delete/{taskId}")
    public OperateResult<String> delTrain(@PathVariable("taskId") String taskId) {
        log.info("[{}] TrainController train delete request. [{}]", taskId, taskId);
        OperateResult<String> result = trainService.delTrain(taskId);
        log.info("[{}] TrainController train delete response. [{}]", taskId, result);
        return result;
    }

    @PostMapping("/train/eval")
    public OperateResult<String> eval(@Validated(Groups.EVAL.class) @RequestBody TrainReq trainReq) {
        log.info("[{}] TrainController train eval request. [{}]", trainReq.getTaskId(), JSONUtil.toJSONString(trainReq));
        OperateResult<String> result = trainService.eval(trainReq);
        log.info("[{}] TrainController train eval response. [{}]", trainReq.getTaskId(), JSONUtil.toJSONString(result));
        return result;
    }

    @PostMapping("/inference/submit")
    public OperateResult<String> inferSubmit(@Validated(Groups.INFERENCE.class) @RequestBody TrainReq trainReq) {
        log.info("[{}] TrainController inference submit request. [{}]", trainReq.getTaskId(), JSONUtil.toJSONString(trainReq));
        OperateResult<String> result = trainService.inferSubmit(trainReq);
        log.info("[{}] TrainController inference submit response. [{}]", trainReq.getTaskId(), JSONUtil.toJSONString(result));
        return result;
    }

    @GetMapping("/inference/delete/{taskId}")
    public OperateResult<String> delInfer(@PathVariable("taskId") String taskId) {
        log.info("[{}] TrainController inference delete request. [{}]", taskId, taskId);
        OperateResult<String> result = trainService.delInfer(taskId);
        log.info("[{}] TrainController inference delete response. [{}]", taskId, result);
        return result;
    }

    @GetMapping("/inference/status/{taskId}")
    public Map<String, String> inferStatus(@PathVariable("taskId") String taskId) {
        log.info("[{}] TrainController inference status request. [{}]", taskId, taskId);
        String podStatus = trainService.inferStatus(taskId);
        Map<String, String> result = new HashMap<>();
        result.put("status", podStatus);
        log.info("[{}] TrainController inference status response. [{}]", taskId, result);
        return result;
    }

    @GetMapping("/intent/sync/{taskId}")
    public OperateResult<String> syncIntentFile(@PathVariable("taskId") String taskId) {
        log.info("[{}] TrainController sync intent file request. [{}]", taskId, taskId);
        OperateResult<String> result = trainService.intentSync(taskId);
        log.info("[{}] TrainController sync intent file response. [{}]", taskId, result);
        return result;
    }
}
