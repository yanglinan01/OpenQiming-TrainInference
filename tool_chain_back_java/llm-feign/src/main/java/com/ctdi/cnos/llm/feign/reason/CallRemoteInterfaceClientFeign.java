package com.ctdi.cnos.llm.feign.reason;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.reason.req.CallRemoteInterfaceReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author zhangxue
 * @description
 * @data 2024/4/23 14:39
 */
@Component
@FeignClient(value = RemoteConstont.REASON_SERVICE_NAME, path = "${cnos.server.llm-reason-service.contextPath}")
public interface CallRemoteInterfaceClientFeign {

    @PostMapping(value = "/call/remote/post")
    String callRemoteDoPostForm(@RequestBody CallRemoteInterfaceReq req);

    @PostMapping(value = "/call/remote/get")
    String callRemoteDoGet(@RequestBody CallRemoteInterfaceReq req);

    @PostMapping(value = "/call/remote/thirdPartyInterfaces")
    Map<String, Object> thirdPartyInterfaces(@RequestParam("url")String url, @RequestParam("message")String message);
}
