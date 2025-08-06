package com.ctdi.cnos.llm.controller.log;

import cn.hutool.core.map.MapUtil;
import com.ctdi.cnos.llm.beans.log.MmModelMonitorStatistics;
import com.ctdi.cnos.llm.beans.log.ModelCallDataReq;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorIntfListReq;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorIntfReq;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorModelReq;
import com.ctdi.cnos.llm.beans.log.model.rsp.MmModelMonitorModelVO;
import com.ctdi.cnos.llm.feign.log.LogServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author caojunhao
 * @DATE 2024/7/5
 */
@Api(tags = { "模型监控接口" })
@RestController
@RequestMapping(value = "/logModel")
@Slf4j
public class ModelLogController {

    @Autowired
    private LogServiceClientFeign logServiceClientFeign;

    @PostMapping(value = "/queryStatistics")
    @ApiOperation(value = "日志概括")
    public OperateResult<MmModelMonitorStatistics> queryStatistics()
    {
        try {
            MmModelMonitorStatistics statistics = logServiceClientFeign.queryStatistics();
            return new OperateResult<>(true, "success", statistics);
        } catch (Exception e) {
            log.error("queryStatistics error:{}", e.getMessage());
            return new OperateResult<>(false, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/queryModelRequest")
    @ApiOperation(value = "模型调用统计")
    public OperateResult<List<MmModelMonitorModelVO>> queryModelRequest(@RequestBody MmModelMonitorModelReq req)
    {
        try {
            return new OperateResult<>(true, "success", logServiceClientFeign.queryModelRequest(req));
        } catch (Exception e) {
            log.error("queryModelRequest error:{}", e.getMessage());
            return new OperateResult<>(false, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/queryModelRequestChart")
    @ApiOperation(value = "接口调用统计 统计图")
    public OperateResult<List<MmModelMonitorModelVO>> queryModelRequestChart(@RequestBody MmModelMonitorIntfReq req)
    {
        try {
            return new OperateResult<>(true, "success", logServiceClientFeign.queryModelRequestChart(req));
        } catch (Exception e) {
            log.error("queryModelRequestChart error:{}", e.getMessage());
            return new OperateResult<>(false, e.getMessage(), null);
        }
    }

    @PostMapping(value = "/queryModelRequestList")
    @ApiOperation(value = "接口调用统计 列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "pageSize",required = false, paramType = "body"),
            @ApiImplicitParam(name = "currentPage", value = "currentPage",required = false, paramType = "body"),
            @ApiImplicitParam(name = "intfCallType", value = "接口调用类型",required = false, paramType = "body", defaultValue = "2"),
            @ApiImplicitParam(name = "taskId", value = "任务id",required = true, paramType = "body"),
            @ApiImplicitParam(name = "startTime", value = "开始时间",required = false, paramType = "body"),
            @ApiImplicitParam(name = "endTime", value = "结束时间",required = false, paramType = "body")
    })
    public OperateResult<Map<String, Object>> queryModelRequestList(@ApiIgnore @RequestBody(required =false) Map<String,String> map)
    {
        try {
            MmModelMonitorIntfListReq req = new MmModelMonitorIntfListReq();
            if (map.get("taskId") != null) {
                req.setTaskId(Long.valueOf(map.get("taskId")));
            }
            if (map.get("intfCallType") != null) {
                req.setIntfCallType(Integer.valueOf(map.get("intfCallType")));
            }
            if (map.get("pageSize") != null && map.get("currentPage") != null) {
                req.setPageSize(Long.valueOf(map.get("pageSize")));
                req.setCurrentPage(Long.valueOf(map.get("currentPage")));
            }
            if (map.get("startTime") != null) {
                req.setStartTime(map.get("startTime"));
            }
            if (map.get("endTime") != null) {
                req.setEndTime(map.get("endTime"));
            }

            Map<String, Object> page = logServiceClientFeign.queryModelRequestList(req);
            return new OperateResult<>(true, "success", page);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new OperateResult<>(false, "error", null);
    }

    @PostMapping(value = "/queryMmTrainLogList")
    @ApiOperation(value = "模型训练日志查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "分页参数",required = false, paramType = "body"),
            @ApiImplicitParam(name = "pageNum", value = "分页参数",required = false, paramType = "body"),
            @ApiImplicitParam(name = "taskId", value = "训练任务id",required = false, paramType = "body")
    })
    public OperateResult<Map<String, Object>> queryMmTrainLogList(@RequestBody Map<String, String> map){
        Map<String, Object> resultMap = logServiceClientFeign.queryMmTrainLogList(map);
        return new OperateResult<>(true, "success", resultMap);
    }

}
