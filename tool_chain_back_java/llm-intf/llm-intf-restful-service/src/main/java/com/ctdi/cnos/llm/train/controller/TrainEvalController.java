package com.ctdi.cnos.llm.train.controller;

import com.alibaba.fastjson.JSON;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.beans.log.MmLog;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEvalCallback;
import com.ctdi.cnos.llm.feign.log.LogServiceClientFeign;
import com.ctdi.cnos.llm.feign.train.TrainTaskEvalServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author huangjinhua
 * @date 2024/5/21 15:49
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/trainEval")
@Api(tags = {"c-eval评估回调接口"})
public class TrainEvalController {

    private static final Logger log = LoggerFactory.getLogger(TrainEvalController.class);

    private final TrainTaskEvalServiceClientFeign trainTaskEvalClient;

    private final LogServiceClientFeign logServiceClientFeign;

    /**
     * 训练任务评估C-EVAL回调接口
     *
     * @param evalCallback eval 对象
     * @return
     */
    @AuthIgnore
    @PostMapping("/evalResult")
    public OperateResult<String> evalResult(@RequestBody TrainTaskEvalCallback evalCallback) {
        MmLog mmLog = logServiceClientFeign.dataAssembly("", "", "训练任务评估C-EVAL回调接口");
        try {
            String evalCallbackJsonStr = JSON.toJSONString(evalCallback);
            mmLog.setRequestParams(evalCallbackJsonStr);
            mmLog.setStatusCode("0");
            mmLog.setInterfaceUrl("intf-restful-service/trainEval/evalResult");

            log.info("评估C-EVAL回调接口入参为:{}", evalCallbackJsonStr);
            if (evalCallback == null || evalCallback.getTaskId() == null) {
                mmLog.setResponseTime(new Date());
                mmLog.setResponseParams("缺失任务ID（taskId）");
                logServiceClientFeign.addLog(mmLog);
                return OperateResult.error("缺失任务ID（taskId）");
            }
            long stime = System.currentTimeMillis();
            OperateResult<String> operateResult = trainTaskEvalClient.callbackUpdateById(evalCallback);
            long etime = System.currentTimeMillis();
            mmLog.setDuration(etime - stime);
            mmLog.setResponseParams(JSON.toJSONString(operateResult));
            mmLog.setResponseTime(new Date());
            logServiceClientFeign.addLog(mmLog);
            return operateResult;
        } catch (Exception exception) {
            log.error("训练任务评估C-EVAL回调接口异常", exception);
            return OperateResult.error(exception.getMessage());
        }
    }
}