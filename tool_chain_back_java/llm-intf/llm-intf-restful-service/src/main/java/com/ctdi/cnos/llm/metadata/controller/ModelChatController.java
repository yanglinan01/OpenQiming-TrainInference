package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.beans.meta.model.ProxyCustomerModelChatDTO;
import com.ctdi.cnos.llm.feign.metadata.ModelChatServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 模型体验HTTP接口。
 *
 * @author laiqi
 * @since 2024/6/19
 */
@Api(tags = {"模型体验"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/model")
@Slf4j
public class ModelChatController {


    private final ModelChatServiceClientFeign modelChatServiceClientFeign;

    @AuthIgnore
    @ApiOperation(value = "代理自建模型体验对话", notes = "代理自建模型体验对话")
    @PostMapping(value = "/proxyCustomerModelChat/{taskId}")
    public ResponseEntity<String> proxyCustomerModelChat(@PathVariable String taskId, @RequestBody ProxyCustomerModelChatDTO request) {
        request.setTaskId(taskId);
        OperateResult<String> result = modelChatServiceClientFeign.proxyCustomerModelChat(request);
        return result.isSuccess() ? ResponseEntity.ok(result.getData()) : ResponseEntity.internalServerError().body(result.getMessage());
    }

}