package com.ctdi.cnos.llm.controller.log;

import cn.hutool.core.map.MapUtil;
import com.ctdi.cnos.llm.beans.log.MmLog;
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

import java.util.Map;

/**
 * @author yuyong
 * @date 2024/4/10 10:15
 */
@Api(tags = { "日志操作接口" })
@RestController
@RequestMapping(value = "/log")
@Slf4j
public class LogController {

    @Autowired
    private LogServiceClientFeign logServiceClientFeign;

    /**
     * 日志列表查询
     * @param mmLog
     * @return
     */
    @ApiOperation(value="查询日志列表")
    @PostMapping(value = "queryList")
    public OperateResult<Map<String, Object>> query(@RequestBody(required = false) MmLog mmLog) {
        try {
            Map<String, Object> retMap = logServiceClientFeign.queryList(mmLog);
            return new OperateResult<>(true, null, retMap);
        } catch (Exception ex) {
            log.error("日志查询异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }

    /**
     * 日志列表分页查询
     * @param map
     * @return
     */
    @ApiOperation(value="分页查询日志[rows:数据清单,total:总条数]")
    @PostMapping(value = "queryPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "pageSize",required = false, paramType = "body"),
            @ApiImplicitParam(name = "currentPage", value = "currentPage",required = false, paramType = "body"),
            @ApiImplicitParam(name = "clientIp", value = "客户端ip",required = false, paramType = "body"),
            @ApiImplicitParam(name = "serverIp", value = "服务端ip",required = false, paramType = "body"),
            @ApiImplicitParam(name = "interfaceName", value = "接口名称",required = false, paramType = "body"),
    })
    public OperateResult<Map<String, Object>> queryPage(@ApiIgnore @RequestBody(required =false) Map<String,String> map) {
        MmLog mmLog = new MmLog();
        mmLog.setClientIp(MapUtil.getStr(map,"clientIp",""));
        mmLog.setInterfaceName(MapUtil.getStr(map,"interfaceName",""));
        mmLog.setServerIp(MapUtil.getStr(map,"serverIp",""));
        long currentPage = MapUtil.getLong(map, "currentPage", 1l);
        long pageSize = MapUtil.getLong(map, "pageSize", 20l);;
        try {
            Map<String, Object> retMap = logServiceClientFeign.queryPage(pageSize, currentPage, mmLog);
            return new OperateResult<>(true, null, retMap);
        } catch (Exception ex) {
            log.error("日志分页查询异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }

    /**
     * 日志新增
     * @param mmLog
     * @return
     */
    @ApiOperation(value="新增日志")
    @PostMapping(value = "addLog")
    public OperateResult<Map<String, Object>> addLog(@RequestBody(required = false) MmLog mmLog) {
        try {
            Map<String, Object> retMap = logServiceClientFeign.addLog(mmLog);
            return new OperateResult<>(true, "新增成功", retMap);
        } catch (Exception ex) {
            log.error("日志新增异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }

    /**
     * 日志组装
     * @param interfaceName
     * @return
     */
    @ApiOperation(value="日志组装")
    @PostMapping(value = "dataAssembly")
    public OperateResult<MmLog> dataAssembly(@RequestBody(required = false) String interfaceName) {
        try {
            MmLog mmlog = logServiceClientFeign.dataAssembly("", "", interfaceName);
            return new OperateResult<>(true, "组装成功", mmlog);
        } catch (Exception ex) {
            log.error("日志新增异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }
}
