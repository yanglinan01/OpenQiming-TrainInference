package com.ctdi.cnos.llm.metadata.service;

import com.ctdi.cnos.llm.beans.reason.req.LlmTimeModelPredictionDTO;
import com.ctdi.cnos.llm.beans.reason.vo.LlmTimeModelPredictionVo;

/**
 * 模型体验。
 *
 * @author laiqi
 * @since 2024/9/4
 */
public interface ModelChatService {

    /**
     * 时序大模型体验预测
     *
     * @param request 时序大模型体验请求对象
     * @return 时序大模型体验预测结果对象
     */
    LlmTimeModelPredictionVo llmTimeModelPrediction(LlmTimeModelPredictionDTO request);

}