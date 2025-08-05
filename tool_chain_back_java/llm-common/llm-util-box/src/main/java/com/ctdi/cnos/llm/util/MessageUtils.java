package com.ctdi.cnos.llm.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangyb
 * @date 2024/6/4 0004 8:58
 * @description MessageUtils
 */
public class MessageUtils {

    /**
     * 获取throw 日志信息
     * @param msg  e.getMessage
     * @return 错误信息
     */
    public static String getMessage(String msg) {
        try {
            int startIndex = msg.indexOf("[{");
            int endIndex = msg.indexOf("}]") + 2;
            String jsonContent = msg.substring(startIndex, endIndex);
            JSONArray jsonArray = JSONArray.parseArray(jsonContent);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            return jsonObject.getString("message");
        } catch (Exception e) {
            return msg;
        }
    }


}
