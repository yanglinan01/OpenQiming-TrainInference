package com.ctdi.cnos.llm.response;

import cn.hutool.http.HttpStatus;
import com.ctdi.cnos.llm.util.MessageUtils;
import feign.FeignException;

public class OperateResult<T> {

    public OperateResult() {
    }

    private boolean success;

    private T data;

    private String message;
    /**
     * http状态码
     */
    private int code;

    /**
     * 为了优化性能，所有没有携带数据的正确结果，均可用该对象表示。
     */
    private static final OperateResult<Void> OK = new OperateResult<>(true, null, null);

    public OperateResult(boolean success, String msg, T data, int code) {
        this.success = success;
        this.message = msg;
        this.data = data;
        this.code = code;
    }

    public OperateResult(boolean success, String msg, T data) {
        this(success, msg, data, HttpStatus.HTTP_OK);
    }

    public OperateResult(boolean success, String msg) {
        this(success, msg, null, HttpStatus.HTTP_OK);

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 创建错误对象。
     * 如果返回错误对象，errorCode 和 errorMessage 分别取自于参数 ResultCode 的 code() 和 message()。
     *
     * @param resultCode 响应码枚举。
     * @return 返回创建的OperateResult实例对象。
     */
    public static <T> OperateResult<T> error(ResultCode resultCode) {
        return error(resultCode.code, resultCode.message());
    }

    /**
     * 创建错误对象。
     *
     * @param operateResult 响应码枚举。
     * @return 返回创建的OperateResult实例对象。
     */
    public static <T> OperateResult<T> error(OperateResult operateResult) {
        return error(operateResult.code, operateResult.getMessage());
    }

    /**
     * 创建错误对象。
     * 如果返回错误对象，errorCode 和 errorMessage 分别取自于参数 errorCodeEnum 的 name() 和 getErrorMessage()。
     *
     * @param errorCodeEnum 错误码枚举。
     * @return 返回创建的OperateResult实例对象。
     */
    public static <T> OperateResult<T> error(ErrorCodeEnum errorCodeEnum) {
        return error(errorCodeEnum.name(), errorCodeEnum.getErrorMessage());
    }

    /**
     * 创建错误对象。
     * 如果返回错误对象，errorCode 和 errorMessage 分别取自于参数 errorCodeEnum 的 name() 和参数 errorMessage。
     *
     * @param errorCodeEnum 错误码枚举。
     * @param errorMessage  自定义的错误信息。
     * @return 返回创建的OperateResult实例对象。
     */
    public static <T> OperateResult<T> error(ErrorCodeEnum errorCodeEnum, String errorMessage) {
        return error(errorCodeEnum.name(), errorMessage);
    }

    /**
     * 创建错误对象。
     *
     * @param errorCode    自定义的错误码。
     * @param errorMessage 自定义的错误信息。
     * @return 返回创建的OperateResult实例对象。
     */
    public static <T> OperateResult<T> error(String errorCode, String errorMessage) {
        return new OperateResult<>(false, errorMessage, null);
    }

    /**
     * 创建错误对象。
     *
     * @param errorCode    自定义的错误码。
     * @param errorMessage 自定义的错误信息。
     * @return 返回创建的OperateResult实例对象。
     */
    public static <T> OperateResult<T> error(int errorCode, String errorMessage) {
        return new OperateResult<>(false, errorMessage, null, errorCode);
    }

    /**
     * 创建错误对象。
     *
     * @param errorMessage 自定义的错误信息。
     * @return 返回创建的OperateResult实例对象。
     */
    public static <T> OperateResult<T> error(String errorMessage) {
        return error("", errorMessage);
    }

    /**
     * 创建Feign异常的错误对象。
     * 如果是Feign异常，则提取错误信息，否则提取异常信息。
     * @param e FeignException
     * @return 返回创建的OperateResult实例对象
     * @param <T> 错误消息
     */
    public static <T> OperateResult<T> errorFromFeign(Exception e) {
        return e instanceof FeignException ? error(MessageUtils.getMessage(e.getMessage())) : error(e.getMessage());
    }

    /**
     * 根据参数中出错的OperateResult，创建新的错误应答对象。
     *
     * @param errorCause 导致错误原因的应答对象。
     * @return 返回创建的OperateResult实例对象。
     */
    public static <T, E> OperateResult<T> errorFrom(OperateResult<E> errorCause) {
        return error("", errorCause.getMessage());
    }

    /**
     * 创建成功对象。
     * 如果需要绑定返回数据，可以在实例化后调用setDataObject方法。
     *
     * @return 返回创建的OperateResult实例对象。
     */
    public static OperateResult<Void> success() {
        return OK;
    }

    /**
     * 创建带有返回数据的成功对象。
     *
     * @param data 返回的数据对象。
     * @return 返回创建的OperateResult实例对象。
     */
    public static <T> OperateResult<T> success(T data) {
        return success(data, null);
    }

    /**
     * 创建带有返回数据的成功对象。
     *
     * @param message 自定义的提示信息。
     * @return 返回创建的OperateResult实例对象。
     */
    public static <T> OperateResult<T> successMessage(String message) {
        return success(null, message);
    }

    /**
     * 创建带有返回数据的成功对象。
     *
     * @param data    返回的数据对象。
     * @param message 自定义的提示信息。
     * @return 返回创建的OperateResult实例对象。
     */
    public static <T> OperateResult<T> success(T data, String message) {
        return new OperateResult<>(true, message, data);
    }
}