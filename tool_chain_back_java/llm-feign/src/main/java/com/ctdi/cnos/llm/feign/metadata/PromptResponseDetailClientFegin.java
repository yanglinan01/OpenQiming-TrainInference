package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptResponseDetail;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yuyong
 * @date 2024/8/19 11:27
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface PromptResponseDetailClientFegin {

    @PostMapping("/promptResponseDetail/addBatch")
    @ApiOperation(value = "新增promptResponseDetail")
    void addBatch(@RequestBody List<PromptResponseDetail> promptResponseDetail);
}
