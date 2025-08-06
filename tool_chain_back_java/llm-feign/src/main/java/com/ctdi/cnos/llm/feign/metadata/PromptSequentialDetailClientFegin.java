package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetail;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yuyong
 * @date 2024/8/16 13:52
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface PromptSequentialDetailClientFegin {

    @PostMapping("/promptSequentialDetail/addBatch")
    @ApiOperation(value = "新增promptSequentialDetail")
    void addBatch(@RequestBody List<PromptSequentialDetail> promptSequentialDetail);
}
