package com.ctdi.cnos.llm.util;


import cn.hutool.core.map.MapUtil;
import com.ctdi.cnos.llm.response.CommonResponse;
import com.ctdi.cnos.llm.response.OperateResult;

import java.util.Map;

/**
 * 通用响应工具类
 */
public class CommonResponseUtil {

    /***
     * 成功响应客户端时调用
     * @param data 返回数据
     * @param <T> 泛型
     * @return
     */
    public static <T> CommonResponse<T> success(T data) {
        CommonResponse<T> commonResponse = new CommonResponse<>();
        commonResponse.setCode(0);
        commonResponse.setData(data);
        commonResponse.setMessage("");
        return commonResponse;
    }


    /**
     * 失败响应客户端时调用
     *
     * @param message 错误消息
     * @return
     */
    public static CommonResponse<String> failure(String message) {
        CommonResponse<String> commonResponse = new CommonResponse<>();
        commonResponse.setCode(-1);
        commonResponse.setMessage(message);
        return commonResponse;
    }

    /**
     * 响应map，新增修改删除操作时使用
     *
     * @param success
     * @param message
     * @return
     */
    public static Map<String, Object> responseMap(boolean success, String message) {
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("success", success);
        map.put("message", message);
        return map;
    }

    /**
     * 响应map，新增修改删除操作时使用
     *
     * @param data 返回数据
     * @param <T>  泛型
     * @return
     */
    public static <T> OperateResult<T> responseSuccess(T data) {
        OperateResult<T> result = new OperateResult<>();
        result.setData(data);
        result.setMessage("");
        result.setSuccess(true);
        return result;
    }

    /**
     * 失败响应客户端时调用
     *
     * @param message 错误消息
     * @return OperateResult
     */
    public static OperateResult<String> responseFailure(String message) {
        OperateResult<String> result = new OperateResult<>();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

    /**
     * 响应map，新增修改删除操作时使用
     *
     * @param message 错误消息
     * @return OperateResult
     */
    public static OperateResult<String> responseSuccess(String message) {
        OperateResult<String> result = new OperateResult<>();
        result.setMessage(message);
        result.setSuccess(true);
        return result;
    }

}
