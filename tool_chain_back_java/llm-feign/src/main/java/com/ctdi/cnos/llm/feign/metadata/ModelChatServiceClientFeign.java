package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.meta.model.ProxyCustomerModelChatDTO;
import com.ctdi.cnos.llm.beans.reason.req.LlmTimeModelPredictionDTO;
import com.ctdi.cnos.llm.beans.reason.vo.LlmTimeModelPredictionVo;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 模型体验服务远程数据操作访问接口。
 *
 * @author laiqi
 * @since 2024/09/04
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface ModelChatServiceClientFeign {

    /**
     * 时序大模型体验预测。
     *
     * @param request 时序大模型体验请求对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/model/llmTimeModelPrediction")
    LlmTimeModelPredictionVo llmTimeModelPrediction(@RequestBody LlmTimeModelPredictionDTO request);

    /**
     * 代理自建模型体验对话
     * @param request 请求对象。
     * @return 原接口响应。
     */
    @PostMapping(value = "/model/proxyCustomerModelChat")
    OperateResult<String> proxyCustomerModelChat(ProxyCustomerModelChatDTO request);
}