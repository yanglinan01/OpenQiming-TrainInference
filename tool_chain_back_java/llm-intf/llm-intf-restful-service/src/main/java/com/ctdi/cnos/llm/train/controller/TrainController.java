package com.ctdi.cnos.llm.train.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.beans.log.MmLog;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTask;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskCallback;
import com.ctdi.cnos.llm.feign.log.LogServiceClientFeign;
import com.ctdi.cnos.llm.feign.train.TrainTaskServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.MessageUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @author yuyong
 * @date 2024/5/21 15:49
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/train")
@Api(tags = {"训练回调接口"})
public class TrainController {

    private static final Logger log = LoggerFactory.getLogger(TrainController.class);

    @Autowired
    private TrainTaskServiceClientFeign trainTaskServiceClientFeign;

    @Autowired
    private LogServiceClientFeign logServiceClientFeign;

    /**
     * 训练回调接口
     *
     * @param taskCallback 任务回调对象
     * @return OperateResult<String>
     */
    @AuthIgnore
    @PostMapping("/trainCallbackInterface")
    public OperateResult<String> trainCallbackInterface(@RequestBody TrainTaskCallback taskCallback) {
        MmLog mmLog = logServiceClientFeign.dataAssembly("", "", "训练回调接口");
        try {
            log.info("回调接口入参为:{}", JSON.toJSONString(taskCallback));
            long stime = System.currentTimeMillis();

            TrainTask updateTask = new TrainTask();
            BeanUtil.copyProperties(taskCallback, updateTask);
            Map<String, Object> result = trainTaskServiceClientFeign.updateById(updateTask);

            long etime = System.currentTimeMillis();
            mmLog.setDuration(etime - stime);
            mmLog.setRequestParams(JSON.toJSONString(taskCallback));
            mmLog.setResponseParams(result.toString());
            mmLog.setResponseTime(new Date());
            mmLog.setStatusCode("0");
            mmLog.setInterfaceUrl("intf-restful-service/train/updateTrainInfo");
            logServiceClientFeign.addLog(mmLog);
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("修改训练任务异常", exception);
            return OperateResult.error(MessageUtils.getMessage(exception.getMessage()));
        }
    }
}