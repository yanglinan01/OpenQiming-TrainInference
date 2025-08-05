package com.ctdi.cnos.llm.log.controller;

import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.beans.log.ModelCallDataReq;
import com.ctdi.cnos.llm.log.service.LogModelMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author HuangGuanSheng
 * @date 2024-07-04 14:37
 */

@Api(tags = {"日志中心-模型监控-调用数据"})
@RestController
@RequestMapping(value = "/logModelMonitor")
public class LogModelMonitorController {

    @Autowired
    LogModelMonitorService logModelMonitorService;

    @AuthIgnore
    @PostMapping("/modelCallData")
    @ApiOperation("模型调用数据")
    public Map<String, Object> modelCallData(@ApiIgnore @RequestBody(required =false)ModelCallDataReq req){
        return logModelMonitorService.insertModelCallData(req);
    }

    @AuthIgnore
    @PostMapping("/deleteMoreThanTimeData")
    @ApiOperation("删除超过15天的数据")
    public String deleteMoreThanTimeData(){
        return logModelMonitorService.deleteMoreThanTimeData();
    }
}
