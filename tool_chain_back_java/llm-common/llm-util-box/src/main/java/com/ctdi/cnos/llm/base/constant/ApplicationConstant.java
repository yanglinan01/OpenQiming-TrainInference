package com.ctdi.cnos.llm.base.constant;

import com.alibaba.fastjson.JSONObject;

/**
 * 。
 *
 * @author laiqi
 * @since 2024/7/15
 */
public interface ApplicationConstant {

    /**
     * 字典服务客户端名称。
     */
    String DICTIONARY_SERVICE_CLIENT_NAME = "com.ctdi.cnos.llm.feign.metadata.DictionaryServiceClientFeign";

    /**
     * 字典服务方法名。
     */
    String DICTIONARY_SERVICE_CLIENT_METHOD_NAME = "getDictItemMap";

    /**
     * 新增 响应成功消息。
     */
    String SAVE_MESSAGE = "新增成功！";
    /**
     * 更新 响应成功消息。
     */
    String UPDATE_MESSAGE = "修改成功！";

    /**
     * 删除 响应成功消息。
     */
    String DELETE_MESSAGE = "删除成功！";


    /**
     * 固定写法：
     */
    JSONObject SYSTEM_MESSAGE = JSONObject.parseObject("{\"role\":\"system\",\"content\":\"You are a helpful assistant. 你是电信的启明大模型，是一个专业的AI助手，你擅长回答用户的QA问题，并且你的回复无害、详细且准确。\"}");


}