/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.controller.webservice;

import cn.hutool.core.map.MapUtil;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.WebServiceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * WebService 控制接口
 *
 * @author huangjinhua
 * @since 2024/4/16
 */
@Api(tags = {"WebService调试接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/webservice")
@Slf4j
public class WebServiceController {

    @ApiOperation(value = "webservice接口调试", notes = "webservice接口调试")
    @PostMapping(value = "/debug")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "webservice 接口地址", required = true, paramType = "param"),
            @ApiImplicitParam(name = "xml", value = "xml格式报文", required = true, paramType = "body")
    })
    public OperateResult<String> debug(@RequestParam(name = "url") String url, @ApiIgnore @RequestBody String xml) {
        try {
            String result = WebServiceUtil.call(url, xml);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("webservice接口调试异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "根据方法名调试webservice接口", notes = "根据方法名调试webservice接口")
    @PostMapping(value = "/debugByFun")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "webservice 接口地址", required = true, paramType = "body"),
            @ApiImplicitParam(name = "funCode", value = "webservice 接口方法名", required = true, paramType = "body"),
            @ApiImplicitParam(name = "params", value = "webservice 接口入参", paramType = "body")
    })
    public OperateResult<String> debug(@ApiIgnore @RequestBody Map<String, String> map) {
        try {
            String result = WebServiceUtil.call(MapUtil.getStr(map, "url"),
                    MapUtil.getStr(map, "funCode"), MapUtil.getStr(map, "params"));
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("webservice接口调试异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }
}
