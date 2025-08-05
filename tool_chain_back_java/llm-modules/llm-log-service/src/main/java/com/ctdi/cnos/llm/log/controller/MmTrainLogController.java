package com.ctdi.cnos.llm.log.controller;

import cn.hutool.core.map.MapUtil;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.log.service.MmTrainLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuyong
 * @date 2024/11/18 16:18
 */
@Api(tags = {"模型训练日志"})
@RestController
@RequestMapping(value = "/trainLog")
public class MmTrainLogController {

    @Autowired
    MmTrainLogService mmTrainLogService;

    /**
     * 分页查询模型训练日志
     * @param map
     * @return
     */
    @AuthIgnore
    @PostMapping("/queryMmTrainLogList")
    @ApiOperation(value = "分页查询模型训练日志记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "分页参数", required = false, paramType = "body"),
            @ApiImplicitParam(name = "pageNum", value = "分页参数", required = false, paramType = "body"),
            @ApiImplicitParam(name = "taskId", value = "训练任务id", required = false, paramType = "body")
    })
    public Map<String, Object> queryMmTrainLogList(@ApiIgnore @RequestBody(required = false) Map<String, String> map) {
//        List<String> list = null;
        Map<String, Object> result = new HashMap<>();
        if(null != map){
            int pageNum = MapUtil.getInt(map, "pageNum", 1);
            int pageSize = MapUtil.getInt(map, "pageSize", 10);
            long taskId = MapUtil.getLong(map, "taskId", 0L);
            result = mmTrainLogService.queryTrainLog(pageNum, pageSize, taskId);
        }
        return result;
    }
}
