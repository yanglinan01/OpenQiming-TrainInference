package com.ctdi.cnos.llm.metadata.extra.conversation.processor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.beans.meta.constant.ApplicationType;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationBodyDTO;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import com.ctdi.cnos.llm.metadata.service.ModelService;
import com.dtflys.forest.Forest;
import com.dtflys.forest.http.ForestRequest;
import org.springframework.stereotype.Component;

/**
 * 系统模型走自建模型的会话实现。
 *
 * @author laiqi
 * @since 2024/9/20
 */
@Component
public class SystemCustomerModelConversationProcessor extends SystemModelConversationProcessor {
    public SystemCustomerModelConversationProcessor(ModelService modelService) {
        super(modelService);
    }

    @Override
    public boolean match(ConversationBodyDTO conversationBody) {
        // 系统走 自定义模型的判定条件
        return ApplicationType.BIG_MODEL == conversationBody.getApplicationType() && ApplicationType.SELF_BUILT_MODEL == getCacheModel(conversationBody).getExperienceImpl();
    }

    /**
     * 刚好跟自建模型参数一致。所以不需要重写
     *
     * @param conversationBody 会话请求体
     * @return 校验结郭
     */
    @Override
    protected boolean validate(ConversationBodyDTO conversationBody) {
        return super.validate(conversationBody);
    }

    @Override
    protected ForestRequest<?> convertForestRequest(ConversationBodyDTO conversationBody, ModelVO model) {
        JSONObject body = conversationBody.buildCustomerModelRequestBody();
        return Forest.post(model.getEndpoint()).contentTypeJson().addBody(body);
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