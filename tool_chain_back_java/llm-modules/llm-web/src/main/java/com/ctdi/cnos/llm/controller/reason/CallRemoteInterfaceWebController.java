package com.ctdi.cnos.llm.controller.reason;

import com.ctdi.cnos.llm.beans.reason.req.CallRemoteInterfaceReq;
import com.ctdi.cnos.llm.feign.reason.CallRemoteInterfaceClientFeign;
import com.ctdi.cnos.llm.util.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangxue
 * @description
 * @data 2024/4/23 9:52
 */
@RestController
@SuppressWarnings({"all"})
@Api(tags = {"调用第三方接口控制层"})
@RequestMapping(value = "/web/call/remote")
public class CallRemoteInterfaceWebController {

    @Autowired
    private CallRemoteInterfaceClientFeign callRemoteInterfaceClientFeign;

    @PostMapping(value = "post")
    @ApiOperation(value = "调用Post类型第三方接口")
    public String callRemoteDoPostForm(@RequestBody CallRemoteInterfaceReq req) {
        return callRemoteInterfaceClientFeign.callRemoteDoPostForm(req);
    }

    @PostMapping(value = "get")
    @ApiOperation(value = "调用Get类型第三方接口")
    public String callRemoteDoGet(@RequestBody CallRemoteInterfaceReq req) {
        return callRemoteInterfaceClientFeign.callRemoteDoGet(req);
    }

}
