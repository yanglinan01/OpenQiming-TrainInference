package com.ctdi.cnos.llm.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据验证失败的自定义异常。
 *
 * @author laiqi
 * @since 2024/09/03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataValidationException extends RuntimeException {

    /**
     * 构造函数。
     */
    public DataValidationException() {

    }

    /**
     * 构造函数。
     *
     * @param msg 错误信息。
     */
    public DataValidationException(String msg) {
        super(msg);
    }
}