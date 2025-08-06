package com.ctdi.cnos.llm.metadata.extra.conversation.processor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.beans.meta.constant.ApplicationType;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationBodyDTO;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTaskVO;
import com.ctdi.cnos.llm.feign.train.DeployTaskServiceClientFeign;
import com.ctdi.cnos.llm.metadata.extra.conversation.BaseConversationProcessor;
import com.dtflys.forest.Forest;
import com.dtflys.forest.http.ForestRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 自建模型对话。
 *
 * 请求示例：
 * <pre>
 *  {
 *     "contents": [
 *         {
 *             "content": "你好",
 *             "contentType": "",
 *             "ext": {},
 *             "role": ""
 *         }
 *     ],
 *     "model": "1831661094859829248",
 *     "modelType": "CUSTOM_MODEL",
 *     "params": {
 *         "top_p": 0.5,
 *         "temperature": 0.2,
 *         "stream": true,
 *     },
 *     "requestId": "1-1",
 *     "sessionId": "1"
 * }
 * }
 * </pre>
 * @see <a href="https://www.hiascend.com/document/detail/zh/mindie/10RC2/mindieservice/servicedev/mindie_service0146.html">接口文档</a>
 * @author laiqi
 * @since 2024/9/14
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CustomerModelConversationProcessor extends BaseConversationProcessor<DeployTaskVO> {

    /**
     * 必填参数
     */
    private static final Set<String> REQUIRED_PARAMS = CollUtil.newHashSet("max_tokens", "temperature", "stream");


    private final DeployTaskServiceClientFeign deployTaskServiceClientFeign;
    @Override
    public boolean match(ConversationBodyDTO conversationBody) {
        return ApplicationType.SELF_BUILT_MODEL == conversationBody.getApplicationType();
    }

    @Override
    protected boolean validate(ConversationBodyDTO conversationBody) {
        JSONObject params = conversationBody.getParams();
        Assert.notNull(params, "参数对象不能为空！");
        Assert.isTrue(CollUtil.containsAll(params.keySet(), REQUIRED_PARAMS), "参数对象缺少必要的参数！" + REQUIRED_PARAMS);
        DeployTaskVO model = getCacheModel(conversationBody);
        Assert.notNull(model, "查询自建模型不存在！");
        Assert.notBlank(model.getDeployUrl(), "该自建模型接口地址不存在，不能体验！");
        return true;
    }

    @Override
    protected DeployTaskVO buildModel(ConversationBodyDTO conversationBody) {
        String modelId = conversationBody.getModel();
        return deployTaskServiceClientFeign.querySimpleById(modelId);
    }

    @Override
    protected ForestRequest<?> convertForestRequest(ConversationBodyDTO conversationBody, DeployTaskVO model) {
        JSONObject body = conversationBody.buildCustomerModelRequestBody();
        return Forest.post(model.getDeployUrl()).contentTypeJson().addBody(body);
    }

    @Override
    protected String extractedContent(String original) {
        if (StrUtil.isEmpty(original)) {
            return original;
        }
        StringBuilder responseMessageBuffer = new StringBuilder();
        JSONObject jsonObject = JSON.parseObject(original);
        JSONArray choices = jsonObject.getJSONArray("choices");
        for (int i = 0; i < choices.size(); i++) {
            JSONObject item = choices.getJSONObject(i);
            String content = item.getJSONObject("message").getString("content");
            responseMessageBuffer.append(StrUtil.removeAllLineBreaks(content));
        }
        return responseMessageBuffer.toString();
    }

}