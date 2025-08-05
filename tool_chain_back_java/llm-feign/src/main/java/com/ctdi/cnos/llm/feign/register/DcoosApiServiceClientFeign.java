package com.ctdi.cnos.llm.feign.register;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.register.DcoosApi;
import com.ctdi.cnos.llm.beans.register.req.DcoosApiQueryReq;
import com.ctdi.cnos.llm.beans.register.req.DcoosApiReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author wangyb
 * @description
 * @data 2024/4/20 16:56
 */
@Component
@FeignClient(value = RemoteConstont.REGISTER_SERVICE_NAME, path = "${cnos.server.llm-register-service.contextPath}")
public interface DcoosApiServiceClientFeign {


    @PostMapping(value = "/dcoosApi/buildDcoosApi")
    String buildDcoosApi(@RequestBody DcoosApi dcoosApi);


    @PostMapping(value = "/dcoosApi/addApi")
    Map<String, Object> addApi(@RequestBody DcoosApiReq req);

    @PostMapping(value = "/dcoosApi/updateApi")
    Map<String, Object> updateApi(@RequestBody DcoosApiReq req);

    @GetMapping(value = "/dcoosApi/queryById/{id}")
    DcoosApi queryById(@RequestParam("id") String id);

    @PostMapping(value = "/dcoosApi/queryList")
    Map<String, Object> queryList(@RequestBody DcoosApiQueryReq req);
}
