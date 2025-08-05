package com.ctdi.cnos.llm.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 参数无效的自定义异常。
 *
 * @author laiqi
 * @since 2024/08/02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArgumentNotValidException extends RuntimeException {

    /**
     * 构造函数。
     */
    public ArgumentNotValidException() {

    }

    /**
     * 构造函数。
     *
     * @param msg 错误信息。
     */
    public ArgumentNotValidException(String msg) {
        super(msg);
    }
}