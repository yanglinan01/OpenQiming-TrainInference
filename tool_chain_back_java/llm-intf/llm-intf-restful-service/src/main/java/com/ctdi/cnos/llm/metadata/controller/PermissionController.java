package com.ctdi.cnos.llm.metadata.controller;

import com.alibaba.fastjson.JSON;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.beans.log.MmLog;
import com.ctdi.cnos.llm.beans.meta.order.OrderDTO;
import com.ctdi.cnos.llm.feign.log.LogServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.OrderServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2024/12/9 15:34
 * @Description
 */
@Api(tags = {"门户用户推送接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/permission")
public class PermissionController {
    private final OrderServiceClientFeign orderServiceClientFeign;

    private final LogServiceClientFeign logServiceClientFeign;

    private static final Logger log = LoggerFactory.getLogger(PermissionController.class);


    @AuthIgnore
    @ApiOperation(value = "获取门户用户权限", notes = "获取门户用户权限")
    @PostMapping(value = "/switch")
    public OperateResult<String> permissionSwitch(@RequestBody OrderDTO orderDTO) {
        OperateResult<String> result = new OperateResult<>();
        MmLog mmLog = logServiceClientFeign.dataAssembly("", "", "获取门户用户权限");
        try {
            String orderDTOStr = JSON.toJSONString(orderDTO);
            log.info("获取门户用户权限入参为:{}", orderDTOStr);
            mmLog.setRequestParams(orderDTOStr);
            mmLog.setStatusCode("0");
            mmLog.setInterfaceUrl("intf-restful-service/permission/switch");
            mmLog.setResponseTime(new Date());
            long stime = System.currentTimeMillis();
            result = orderServiceClientFeign.syncOrder(orderDTO);
            long etime = System.currentTimeMillis();
            mmLog.setDuration(etime - stime);
            mmLog.setResponseParams(JSON.toJSONString(result));
            mmLog.setResponseTime(new Date());
        } catch (Exception e){
            mmLog.setResponseParams("message:" + e.getMessage() + "stackTrace:" + e.getStackTrace());
        }finally {
            logServiceClientFeign.addLog(mmLog);
            if(!result.isSuccess()){
                result=OperateResult.error("获取门户用户权限接口异常");
            }
        }
        return result;
    }
}
