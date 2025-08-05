package com.ctdi.cnos.llm.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.common.core.utils.poi.ExcelUtil;
import com.ctdi.cnos.llm.beans.job.MmJobLog;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.service.MmJobLogService;
import com.ctdi.cnos.llm.util.CommonResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调度日志操作处理
 *
 * @author huangjinhua
 * @since 2024/6/3
 */
@Api(tags = {"定时任务调度日志操作"})
@RestController
@RequestMapping("/jobLog")
@RequiredArgsConstructor
public class MmJobLogController {
    private final MmJobLogService jobLogService;


    @ApiOperation(value = "分页查询定时任务调度日志", notes = "分页查询定时任务调度")
    @GetMapping("/queryPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName", value = "任务名称", paramType = "param"),
            @ApiImplicitParam(name = "pageSize", value = "页大小，默认为20", paramType = "param"),
            @ApiImplicitParam(name = "currentPage", value = "当前页，默认为1", paramType = "param"),
    })
    public Map<String, Object> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                         @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                         @RequestParam(name = "jobName", required = false) String jobName) {
        Page<MmJobLog> page = new Page<>(currentPage, pageSize);
        page.addOrder(OrderItem.desc("a.create_date"));
        MmJobLog jobLog = new MmJobLog();
        jobLog.setJobName(jobName);
        jobLogService.queryPage(page, jobLog);
        Map<String, Object> result = new HashMap<>(2);
        result.put("total", page.getTotal());
        result.put("rows", page.getRecords());
        return result;
    }

    @ApiOperation(value = "查询定时任务调度日志列表", notes = "查询定时任务调度日志列表")
    @GetMapping("/queryList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName", value = "任务名称", paramType = "param")
    })
    public List<MmJobLog> queryList(MmJobLog jobLog) {
        return jobLogService.queryList(jobLog);
    }

    @ApiOperation(value = "导出定时任务调度日志列表", notes = "导出定时任务调度日志列表")
    @PostMapping("/export")
    public void export(HttpServletResponse response, MmJobLog jobLog) {
        List<MmJobLog> list = jobLogService.queryList(jobLog);
        ExcelUtil<MmJobLog> util = new ExcelUtil<>(MmJobLog.class);
        util.exportExcel(response, list, "调度日志");
    }

    @ApiOperation(value = "根据调度编号获取详细信息", notes = "根据调度编号获取详细信息")
    @GetMapping(value = "/detail")
    public MmJobLog detail(@RequestParam("id") Long id) {
        return jobLogService.queryById(id);
    }

    @ApiOperation(value = "删除定时任务调度日志", notes = "删除定时任务调度日志")
    @DeleteMapping("/delete")
    public OperateResult<String> delete(@RequestParam("ids") String ids) {
        List<Long> logIds = Convert.toList(Long.class, CharSequenceUtil.split(ids, ","));
        jobLogService.deleteByIds(logIds);
        return CommonResponseUtil.responseSuccess("删除定时任务调度日志成功");
    }

    @ApiOperation(value = "清空定时任务调度日志", notes = "清空定时任务调度日志")
    @DeleteMapping("/cleanAll")
    public OperateResult<String> cleanAll() {
        jobLogService.cleanJobLog();
        return CommonResponseUtil.responseSuccess("清空定时任务调度日志");
    }
}
