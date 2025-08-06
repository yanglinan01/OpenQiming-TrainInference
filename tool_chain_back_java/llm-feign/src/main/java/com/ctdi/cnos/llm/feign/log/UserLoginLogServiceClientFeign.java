package com.ctdi.cnos.llm.feign.log;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 用户登录日志服务远程数据操作访问接口。
 *
 * @author laiqi
 * @since 2024/10/15
 */
@Component
@FeignClient(value = RemoteConstont.LOG_SERVICE_NAME, path = "${cnos.server.llm-log-service.contextPath}")
public interface UserLoginLogServiceClientFeign {

    /**
     * 根据类型统计 日活用户数
     * DAY: 统计当前日期近31天每天的日活用户数
     * MONTH: 统计当前日期前12个月每月的日活用户数
     *
     * @param type 统计类型。
     * @return 统计数据。
     */
    @GetMapping(value = "/userLoginLog/queryChart")
    OperateResult<Map<String, Long>> queryChart(@RequestParam("type") StatType type);

}