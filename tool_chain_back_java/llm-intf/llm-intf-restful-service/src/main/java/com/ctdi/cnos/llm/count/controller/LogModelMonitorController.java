package com.ctdi.cnos.llm.count.controller;

import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.beans.log.ModelCallDataReq;
import com.ctdi.cnos.llm.feign.log.LogServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author HuangGuanSheng
 * @date 2024-07-04 14:18
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/logModelMonitor")
@Api(tags = {"日志中心-模型监控"})
@Slf4j
public class LogModelMonitorController {

    private final LogServiceClientFeign logServiceClientFeign;

    @AuthIgnore
    @PostMapping("/modelCallData")
    @ApiOperation("模型调用数据")
    public OperateResult<Map<String, Object>> modelCallData(@ApiIgnore @RequestBody(required =false)ModelCallDataReq req){
        log.info("模型调用数据:"+req);
        Map<String, Object> stringObjectMap = logServiceClientFeign.modelCallData(req);
        return new OperateResult<>(true, null, stringObjectMap);
    }

}
