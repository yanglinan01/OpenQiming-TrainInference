package com.ctdi.llmtc.xtp.traininfer.util;

import com.ctdi.llmtc.xtp.traininfer.constant.StatusEnum;
import lombok.Data;

@Data
public class OperateResult2<T> {

    private static final String SUCCEEDED = "Succeeded";
    private static final String ERROR = "Error";
    private static final String FAILED = "Failed";

    public OperateResult2() {
    }

    private String status;

    private T data;


    public OperateResult2(String status) {
        this.status = status;
        this.data =  (T) "";
    }

    public OperateResult2(String status, T data) {
        this.status = status;
        this.data = (data == null && isStringType(data)) ? (T) "" : data;;
    }

    public static <T> OperateResult2<T> error(T data) {
        return new OperateResult2<>(ERROR, data);
    }

    public static <T> OperateResult2<T> success() {
        return new OperateResult2<>(SUCCEEDED);
    }

    public static <T> OperateResult2<T> success(T data) {
        return new OperateResult2<>(SUCCEEDED, data);
    }

    public static <T> OperateResult2<T> fail(T data) {
        return new OperateResult2<>(FAILED, data);
    }

    public static OperateResult2<String> fail(StatusEnum statusEnum) {
        return new OperateResult2<>(FAILED, statusEnum.getMsg());
    }

    public static OperateResult2<String> error(StatusEnum statusEnum) {
        return new OperateResult2<>(ERROR, statusEnum.getMsg());
    }

    private boolean isStringType(T t) {
        return t instanceof String ||
                this.getClass().getTypeParameters()[0].getTypeName().equals("java.lang.String");
    }

}
