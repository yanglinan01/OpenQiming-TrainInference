package com.ctdi.cnos.llm.feign.intfService;

import com.ctdi.cnos.llm.RemoteConstont;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author hanfulei
 * @Date 2024/4/18 15:37
 * @Version 1.0
 **/
@Component
@FeignClient(value = RemoteConstont.INTF_RESTFUL_SERVICE_NAME, path = "${cnos.server.llm-intf-restful-service.contextPath}")
public interface DcossToolHttpClientFeign {

    @PostMapping(value = "/dcossTool/httpClient/{key}")
    String dcossHttpTool(@PathVariable("key") String key, @RequestBody String xml);

    @PostMapping("/history/clean")
    public String cleanHistoryData();

}
