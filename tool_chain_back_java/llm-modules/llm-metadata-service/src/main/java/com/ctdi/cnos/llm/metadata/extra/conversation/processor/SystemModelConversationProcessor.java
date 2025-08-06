package com.ctdi.cnos.llm.metadata.extra.conversation.processor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.beans.meta.constant.ApplicationType;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationBodyDTO;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import com.ctdi.cnos.llm.metadata.extra.conversation.BaseConversationProcessor;
import com.ctdi.cnos.llm.metadata.service.ModelService;
import com.dtflys.forest.Forest;
import com.dtflys.forest.http.ForestRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 系统模型对话。
 * <p>
 * 请求示例：
 * <pre>
 *     {
 *     "contents": [
 *         {
 *             "content": "你好",
 *             "contentType": "",
 *             "ext": {},
 *             "role": ""
 *         }
 *     ],
 *     "model": "1790249036662829056",
 *     "modelType": "SYSTEM_MODEL",
 *     "params": {
 *         "top_p": 0.5,
 *         "temperature": 0.2,
 *         "stream": true,
 *         "stop_token_ids": [151645, 151644, 151643],
 *         "max_tokens": 1000,
 *         "top_k": 1
 *     },
 *     "requestId": "1-1",
 *     "sessionId": "1"
 * }
 * </pre>
 *
 * @author laiqi
 * @see <a href="https://docs.qq.com/doc/DUERKR01oZ2RPT1JV?login_t=1718758811072">接口文档</a>
 * @since 2024/9/14
 */
@RequiredArgsConstructor
@Component
public class SystemModelConversationProcessor extends BaseConversationProcessor<ModelVO> {

    /**
     * 必填参数
     */
    private static final Set<String> REQUIRED_PARAMS = CollUtil.newHashSet("max_tokens", "temperature", "stream");

    private final ModelService modelService;

    @Override
    public boolean match(ConversationBodyDTO conversationBody) {
        return ApplicationType.BIG_MODEL == conversationBody.getApplicationType() && ApplicationType.BIG_MODEL == getCacheModel(conversationBody).getExperienceImpl();
    }

    @Override
    protected boolean validate(ConversationBodyDTO conversationBody) {
        JSONObject params = conversationBody.getParams();
        Assert.notNull(params, "参数对象不能为空！");
        Assert.isTrue(CollUtil.containsAll(params.keySet(), REQUIRED_PARAMS), "参数对象缺少必要的参数！" + REQUIRED_PARAMS);
        ModelVO model = getCacheModel(conversationBody);
        Assert.notNull(model, "查询模型不存在！");
        Assert.notBlank(model.getEndpoint(), "该模型接口地址有误！");
        return true;
    }

    @Override
    protected ModelVO buildModel(ConversationBodyDTO conversationBody) {
        String modelId = conversationBody.getModel();
        return modelService.queryById(Long.valueOf(modelId));
    }

    @Override
    protected ForestRequest<?> convertForestRequest(ConversationBodyDTO conversationBody, ModelVO model) {
        JSONObject body = new JSONObject();
        JSONObject params = conversationBody.getParams();
        body.put("session_id", conversationBody.getSessionId());
        body.put("question", conversationBody.getContents().get(0).getContent());
        body.put("stream", params.getBooleanValue("stream"));
        if (params.containsKey("top_p")) {
            body.put("top_p", params.getFloat("top_p"));
        }
        // 温度
        float temperature = 0.8F;
        if (params.containsKey("temperature")) {
            temperature = params.getFloat("temperature");
        }
        body.put("temperature", temperature);
        int[] stop_token_ids = null;
        if (params.containsKey("stop_token_ids")) {
            stop_token_ids = params.getObject("stop_token_ids", int[].class);
        } else {
            String stopTokenIds = model.getStopTokenIds();
            if (StrUtil.isNotEmpty(stopTokenIds)) {
                stop_token_ids = StrUtil.splitToInt(stopTokenIds, ",");
            }
        }
        if (ArrayUtil.isNotEmpty(stop_token_ids)) {
            body.put("stop_token_ids", stop_token_ids);
        }
        int max_tokens = 1000;
        if (params.containsKey("max_tokens")) {
            max_tokens = params.getInteger("max_tokens");
        }
        body.put("max_tokens", ObjUtil.defaultIfNull(max_tokens, model.getMaxTokens()));
        body.put("top_k", ObjUtil.defaultIfNull(params.getInteger("top_k"), model.getTopK()));
        return Forest.post(model.getEndpoint()).contentTypeJson().addBody(body);
    }

    @Override
    protected String extractedContent(String original) {
        List<String> allGroup1 = ReUtil.findAllGroup1(MetaDataConstants.MODEL_CHAT_PATTERN, original);
        return StrUtil.removePrefix(allGroup1.get(2), "assistant\\n");
    }
}