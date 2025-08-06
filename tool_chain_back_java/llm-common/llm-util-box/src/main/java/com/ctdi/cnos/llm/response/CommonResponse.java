package com.ctdi.cnos.llm.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <h1>通用响应对象定义</h1>
 * {
 *     "code":0,
 *     "message":"",
 *     "data":{}
 * }
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> implements Serializable {

    /** 错误编码 -1 表示失败， 0 表示成功*/
    private Integer code;

    /**错误消息*/
    private String message;

    /**泛型响应数据*/
    private T data;


}
