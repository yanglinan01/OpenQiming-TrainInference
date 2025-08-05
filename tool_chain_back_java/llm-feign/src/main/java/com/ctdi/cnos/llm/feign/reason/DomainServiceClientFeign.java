package com.ctdi.cnos.llm.feign.reason;


import com.ctdi.cnos.llm.RemoteConstont;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author XX
 * @version 1.0.0
 * @ClassName AddressDataOperServiceCient.java
 * @Description TODO
 * @createTime 2021-12-29-10:55:00
 */
@Component
@FeignClient(value = RemoteConstont.REASON_SERVICE_NAME, path = "${cnos.server.llm-reason-service.contextPath}")
public interface DomainServiceClientFeign {
    @PostMapping(value = "/domain/query")
    public Map<String, Object> query(@RequestBody(required = false) Map param);

}
