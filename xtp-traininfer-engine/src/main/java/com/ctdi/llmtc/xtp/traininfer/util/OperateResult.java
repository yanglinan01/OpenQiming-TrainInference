package com.ctdi.llmtc.xtp.traininfer.util;

import com.ctdi.llmtc.xtp.traininfer.constant.StatusEnum;
import lombok.Data;

@Data
public class OperateResult<T> {

    private static final String SUCCEEDED = "Succeeded";
    private static final String ERROR = "Error";
    private static final String FAILED = "Failed";

    public OperateResult() {
    }

    private String status;

    private T info;


    public OperateResult(String status) {
        this.status = status;
        this.info =  (T) "";
    }

    public OperateResult(String status, T info) {
        this.status = status;
        this.info = (info == null && isStringType(info)) ? (T) "" : info;;
    }

    public static <T> OperateResult<T> error(T info) {
        return new OperateResult<>(ERROR, info);
    }

    public static <T> OperateResult<T> success() {
        return new OperateResult<>(SUCCEEDED);
    }

    public static <T> OperateResult<T> success(T info) {
        return new OperateResult<>(SUCCEEDED, info);
    }

    public static <T> OperateResult<T> fail(T info) {
        return new OperateResult<>(FAILED, info);
    }

    public static OperateResult<String> fail(StatusEnum statusEnum) {
        return new OperateResult<>(FAILED, statusEnum.getMsg());
    }

    public static OperateResult<String> error(StatusEnum statusEnum) {
        return new OperateResult<>(ERROR, statusEnum.getMsg());
    }

    private boolean isStringType(T t) {
        return t instanceof String ||
                this.getClass().getTypeParameters()[0].getTypeName().equals("java.lang.String");
    }
}
