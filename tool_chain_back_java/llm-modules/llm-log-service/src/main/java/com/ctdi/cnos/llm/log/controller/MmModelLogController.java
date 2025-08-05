package com.ctdi.cnos.llm.log.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.beans.log.MmModelMonitorStatistics;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorIntfListReq;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorIntfReq;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorModelReq;
import com.ctdi.cnos.llm.beans.log.model.rsp.MmModelMonitorModelListVO;
import com.ctdi.cnos.llm.beans.log.model.rsp.MmModelMonitorModelVO;
import com.ctdi.cnos.llm.log.service.MmModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author caojunhao
 * @DATE 2024/7/4
 */
@Api(tags = {"日志中心-模型监控 接口"})
@RestController
@RequestMapping(value = "/logModel")
public class MmModelLogController {

    @Autowired
    private MmModelService modelService;

    @ApiOperation(value = "日志概括")
    @RequestMapping(value = "/queryStatistics")
    public MmModelMonitorStatistics queryStatistics()
    {
        return modelService.queryStatistics();
    }

    @ApiOperation(value = "模型调用统计")
    @RequestMapping(value = "/queryModelRequest")
    public List<MmModelMonitorModelVO> queryModelRequest(@RequestBody MmModelMonitorModelReq req)
    {
        try {
            if (req.getTaskId() == null) {
                return new ArrayList<>();
            }
            return modelService.queryModelRequest(req);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "接口调用统计 统计图")
    @RequestMapping(value = "/queryModelRequestChart")
    public List<MmModelMonitorModelVO> queryModelRequestChart(@RequestBody MmModelMonitorIntfReq req)
    {
        try {
            if (req.getTaskId() == null) {
                return new ArrayList<>();
            }
            return modelService.queryModelRequestChart(req);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "接口调用统计 列表")
    @PostMapping(value = "/queryModelRequestList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页大小，默认为20", paramType = "param"),
            @ApiImplicitParam(name = "currentPage", value = "当前页，默认为1", paramType = "param")
    })
    public Map<String, Object> queryModelRequestList(@RequestBody MmModelMonitorIntfListReq req)
    {
        Map<String, Object> result = new HashMap<>(2);
        if (req.getTaskId() == null) {
            result.put("total", 0);
            result.put("rows", new ArrayList<>());
            result.put("pages", 0);
            return result;
        }
        Page<MmModelMonitorModelListVO> page = new Page<>(req.getCurrentPage(), req.getPageSize());
        try {
            modelService.queryIntfPage(page, req, TrainConstants.DEPLOY_BELONG_TOOL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        result.put("total", page.getTotal());
        result.put("rows", page.getRecords());
        result.put("pages", page.getPages());
        return result;
    }

}
