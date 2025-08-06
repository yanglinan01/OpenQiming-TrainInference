package com.ctdi.cnos.llm.log.controller;

import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.system.user.service.UserLoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 用户登录日志 控制器类。
 *
 * @author laiqi
 * @since 2024/10/15
 */
@Api(tags = "用户登录日志接口", value = "UserLoginLogController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/userLoginLog")
public class UserLoginLogController {

    private final UserLoginLogService service;

    /**
     * 统计平台用户登录数量画图。
     *
     * @param type day：统计当前日期近31天每天的日活用户数；month：统计当前日期前12个月每月的日活用户数
     * @return 统计数据
     */
    @ApiOperation(value = "统计平台用户登录数据画图", notes = "统计平台用户登录数量画图")
    @GetMapping(value = "/queryChart")
    public OperateResult<Map<String, Long>> queryChart(@ApiParam(value = "统计类型，DAY：统计当前日期近31天每天的日活用户数；MONTH：统计当前日期前12个月每月的日活用户数", required = true, example = "DAY")
                                                              @NotNull(message = "统计类型不能为空") @RequestParam("type") StatType type) {
        return service.queryChart(type);
    }

}