package com.ctdi.cnos.llm.beans.reason.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author zhangxue
 * @description
 * @data 2024/4/23 10:21
 */
@Data
@SuppressWarnings("all")
@ApiModel(value = "调用远端接口入参")
public class CallRemoteInterfaceReq {
    /**
     * 远端接口地址
     */
    @ApiModelProperty(value = "远端接口地址")
    String url;

    /**
     * 远端接口请求参数
     */
    @ApiModelProperty(value = "远端接口请求参数")
    Map<String, Object> requestBody;
}
