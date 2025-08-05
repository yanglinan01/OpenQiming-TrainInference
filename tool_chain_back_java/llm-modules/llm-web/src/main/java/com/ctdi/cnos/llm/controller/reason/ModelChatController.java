package com.ctdi.cnos.llm.controller.reason;

import com.ctdi.cnos.llm.beans.reason.req.LlmTimeModelPredictionDTO;
import com.ctdi.cnos.llm.beans.reason.vo.LlmTimeModelPredictionVo;
import com.ctdi.cnos.llm.feign.metadata.ModelChatServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 大模型体验HTTP接口。
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

    /**
     * 时序大模型体验预测。
     *
     * @param request 时序大模型体验请求对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "时序大模型体验预测", notes = "时序大模型体验预测")
    @PostMapping(value = "/llmTimeModelPrediction")
    public OperateResult<LlmTimeModelPredictionVo> llmTimeModelPrediction(@Validated @RequestBody LlmTimeModelPredictionDTO request) {
        LlmTimeModelPredictionVo result = modelChatServiceClientFeign.llmTimeModelPrediction(request);
        return OperateResult.success(result);
    }
}