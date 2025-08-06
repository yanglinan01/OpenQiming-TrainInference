package com.ctdi.cnos.llm.log.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.beans.log.MmLog;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.log.service.MmLogService;
import com.ctdi.cnos.llm.util.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuyong
 * @date 2024/4/3 17:17
 */
@Api(tags = {"日志记录接口"})
@RestController
@RequestMapping(value = "/log")
public class MmLogController {

    @Autowired
    MmLogService mmLogService;

    @PostMapping("/queryList")
    @ApiOperation(value = "查询日志记录列表")
    public List<MmLog> queryList(@RequestBody MmLog mmLog) {
        List<MmLog> list = mmLogService.queryList(mmLog);
        return list;
    }

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询日志记录列表")
    public Map<String, Object> queryPage(@RequestParam("pageSize") long pageSize,
                                         @RequestParam("currentPage") long currentPage,
                                         @RequestBody(required = false) MmLog mmLog) {
        Page<MmLog> page = new Page<>(currentPage, pageSize);
//        List<MmLog> list = mmLogService.queryList(page, mmLog);
        page.setRecords(mmLogService.queryList(page, mmLog));
        Map<String, Object> result = new HashMap<>(2);
        result.put("total", page.getTotal());
        result.put("rows", page.getRecords());
        return result;
    }

    @AuthIgnore
    @PostMapping("/addLog")
    @ApiOperation(value = "新增日志记录信息")
    public Map<Object, Object> addLog(@RequestBody MmLog mmLog) {
        Map<Object, Object> result = new HashMap<>(2);
        mmLogService.insert(mmLog);
        result.put(1, "新增成功");
        return result;
    }

    @GetMapping("/delLog/{id}")
    @ApiOperation(value = "删除日志记录信息")
    public Map<Object, Object> deleteById(@PathVariable("id") BigDecimal id) {
        Map<Object, Object> result = new HashMap<>(2);
        mmLogService.deleteById(id);
        result.put(1, "删除成功");
        return result;
    }

    @PostMapping("/updateLog")
    @ApiOperation(value = "更新日志记录信息")
    public Map<Object, Object> updateLog(@RequestBody MmLog mmLog) {
        Map<Object, Object> result = new HashMap<>(2);
        mmLogService.updateById(mmLog);
        result.put(1, "修改成功");
        return result;
    }

    @GetMapping("/queryLog/{id}")
    @ApiOperation(value = "查询日志记录信息")
    public MmLog queryById(@PathVariable("id") BigDecimal id) {
        MmLog mmLog = mmLogService.queryById(id);
        return mmLog;
    }

    @AuthIgnore
    @PostMapping("/dataAssembly")
    @ApiOperation(value = "新增日志记录信息")
    public MmLog dataAssembly(@RequestParam("createId") String createId, @RequestParam("modifierId") String modifierId, @RequestParam("interfaceName") String interfaceName) {
        MmLog mmLog = new MmLog();
        Date nowTime = new Date();
        mmLog.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
        mmLog.setCreateDate(nowTime);
        if (null != UserContextHolder.getUser()) {
            String userId = Convert.toStr(UserContextHolder.getUser().getId());
            mmLog.setCreatorId(userId);
            mmLog.setModifierId(userId);
        } else {
            mmLog.setCreatorId(createId);
            mmLog.setModifierId(modifierId);
            if (StringUtils.isBlank(mmLog.getCreatorId())) {
                mmLog.setCreatorId("-1");
            }
            if (StringUtils.isBlank(mmLog.getModifierId())) {
                mmLog.setModifierId("-1");
            }
        }
        mmLog.setModifyDate(nowTime);
        mmLog.setInterfaceName(interfaceName);
        mmLog.setRequestTime(nowTime);
        mmLog.setClientIp(HttpUtil.getClientIp());
        mmLog.setServerIp(NetUtil.getLocalhostStr()/* HttpUtil.getServerIp() */);
        return mmLog;
    }
}