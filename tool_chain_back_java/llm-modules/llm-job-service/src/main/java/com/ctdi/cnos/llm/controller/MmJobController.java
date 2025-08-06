package com.ctdi.cnos.llm.controller;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.common.core.constant.Constants;
import com.ctdi.cnos.common.core.exception.job.TaskException;
import com.ctdi.cnos.common.core.utils.poi.ExcelUtil;
import com.ctdi.cnos.llm.beans.job.MmJob;
import com.ctdi.cnos.llm.constants.JobConstants;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.service.MmJobService;
import com.ctdi.cnos.llm.util.CommonResponseUtil;
import com.ctdi.cnos.llm.utils.CronUtils;
import com.ctdi.cnos.llm.utils.ScheduleUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调度任务信息操作处理
 *
 * @author ruoyi
 */
@Api(tags = {"定时任务调度接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/job")
public class MmJobController {
    private final MmJobService jobService;

    @ApiOperation(value = "分页查询定时任务调度", notes = "分页查询定时任务调度")
    @GetMapping("/queryPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName", value = "任务名称", paramType = "param"),
            @ApiImplicitParam(name = "pageSize", value = "页大小，默认为20", paramType = "param"),
            @ApiImplicitParam(name = "currentPage", value = "当前页，默认为1", paramType = "param"),
    })
    public Map<String, Object> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                         @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                         @RequestParam(name = "jobName", required = false) String jobName) {
        Page<MmJob> page = new Page<>(currentPage, pageSize);
        page.addOrder(OrderItem.desc("a.modify_date"));
        MmJob job = new MmJob();
        job.setJobName(jobName);
        jobService.queryPage(page, job);
        Map<String, Object> result = new HashMap<>(2);
        result.put("total", page.getTotal());
        result.put("rows", page.getRecords());
        return result;
    }

    @ApiOperation(value = "查询定时任务调度列表", notes = "查询定时任务调度列表")
    @GetMapping("/queryList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName", value = "任务名称", paramType = "param")
    })
    public List<MmJob> queryList(@RequestParam(required = false) MmJob mmJob) {
        return jobService.queryList(mmJob);
    }


    @ApiOperation(value = "导出定时任务列表", notes = "导出定时任务列表")
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody(required = false) MmJob mmJob) {
        List<MmJob> list = jobService.queryList(mmJob);
        ExcelUtil<MmJob> util = new ExcelUtil<>(MmJob.class);
        util.exportExcel(response, list, "定时任务");
    }

    @ApiOperation(value = "获取定时任务详细信息", notes = "获取定时任务详细信息")
    @GetMapping(value = "/detail")
    public MmJob detail(@RequestParam("id") Long id) {
        return jobService.queryById(id);
    }

    @ApiOperation(value = "新增定时任务", notes = "新增定时任务")
    @PostMapping(value = "/add")
    public OperateResult<String> add(@RequestBody MmJob mmJob) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(mmJob.getCronExpression())) {
            return CommonResponseUtil.responseFailure("新增任务'" + mmJob.getJobName() + "'失败，Cron表达式不正确");
        } else if (StringUtils.isBlank(mmJob.getInvokeTarget())) {
            return CommonResponseUtil.responseFailure("新增任务'" + mmJob.getJobName() + "'失败，缺乏目标字符串");
        } else if (StringUtils.containsIgnoreCase(mmJob.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            return CommonResponseUtil.responseFailure("新增任务'" + mmJob.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        } else if (StringUtils.containsAnyIgnoreCase(mmJob.getInvokeTarget(), new String[]{Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            return CommonResponseUtil.responseFailure("新增任务'" + mmJob.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(mmJob.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})) {
            return CommonResponseUtil.responseFailure("新增任务'" + mmJob.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(mmJob.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            return CommonResponseUtil.responseFailure("新增任务'" + mmJob.getJobName() + "'失败，目标字符串存在违规");
        } else if (!ScheduleUtils.whiteList(mmJob.getInvokeTarget())) {
            return CommonResponseUtil.responseFailure("新增任务'" + mmJob.getJobName() + "'失败，目标字符串不在白名单内");
        }
        if (mmJob.getMisfirePolicy() == null) {
            mmJob.setMisfirePolicy(JobConstants.MISFIRE_DEFAULT);
        }
        mmJob.setCreatorId(UserContextHolder.getUserId());
        mmJob.setCreateDate(DateUtil.date());
        mmJob.setModifierId(UserContextHolder.getUserId());
        mmJob.setModifyDate(DateUtil.date());
        jobService.insertJob(mmJob);
        return CommonResponseUtil.responseSuccess("新增任务成功");
    }

    @ApiOperation(value = "修改定时任务", notes = "修改定时任务")
    @PostMapping(value = "/update")
    public OperateResult<String> update(@RequestBody MmJob mmJob) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(mmJob.getCronExpression())) {
            return CommonResponseUtil.responseFailure("修改任务'" + mmJob.getJobName() + "'失败，Cron表达式不正确");
        } else if (StringUtils.containsIgnoreCase(mmJob.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            return CommonResponseUtil.responseFailure("修改任务'" + mmJob.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        } else if (StringUtils.containsAnyIgnoreCase(mmJob.getInvokeTarget(), new String[]{Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            return CommonResponseUtil.responseFailure("修改任务'" + mmJob.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(mmJob.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})) {
            return CommonResponseUtil.responseFailure("修改任务'" + mmJob.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(mmJob.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            return CommonResponseUtil.responseFailure("修改任务'" + mmJob.getJobName() + "'失败，目标字符串存在违规");
        } else if (StringUtils.isNotBlank(mmJob.getInvokeTarget()) && !ScheduleUtils.whiteList(mmJob.getInvokeTarget())) {
            return CommonResponseUtil.responseFailure("修改任务'" + mmJob.getJobName() + "'失败，目标字符串不在白名单内");
        }
        if (mmJob.getMisfirePolicy() == null) {
            mmJob.setMisfirePolicy(JobConstants.MISFIRE_DEFAULT);
        }
        mmJob.setModifierId(UserContextHolder.getUserId());
        mmJob.setModifyDate(DateUtil.date());
        jobService.updateJob(mmJob);
        return CommonResponseUtil.responseSuccess("修改任务成功");
    }

    @ApiOperation(value = "修改定时任务状态", notes = "修改定时任务状态")
    @PostMapping("/changeStatus")
    public OperateResult<String> changeStatus(@RequestBody MmJob mmJob) throws SchedulerException {
        MmJob newMmJob = jobService.queryById(mmJob.getId());
        newMmJob.setStatus(mmJob.getStatus());
        newMmJob.setModifyDate(DateUtil.date());
        jobService.changeStatus(newMmJob);
        return CommonResponseUtil.responseSuccess("修改任务状态修成功");
    }

    @ApiOperation(value = "定时任务立即执行一次", notes = "定时任务立即执行一次")
    @GetMapping("/run")
    public OperateResult<String> run(@RequestParam("id") Long id) throws SchedulerException, TaskException {
        MmJob job = jobService.queryById(id);
        if(job != null){
            jobService.run(job);
            return CommonResponseUtil.responseSuccess("任务执行成功");
        }else{
            return CommonResponseUtil.responseFailure("任务不存在！");
        }
    }

    @ApiOperation(value = "删除定时任务", notes = "删除定时任务")
    @DeleteMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "任务ID，多个英文逗号分隔", paramType = "param")
    })
    public OperateResult<String> delete(@RequestParam("ids") String ids) throws SchedulerException {
        List<Long> jobIds = Convert.toList(Long.class, CharSequenceUtil.split(ids, ","));
        jobService.deleteByIds(jobIds);
        return CommonResponseUtil.responseSuccess("任务删除成功");
    }

}
