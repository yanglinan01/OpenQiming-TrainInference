package com.ctdi.cnos.llm.base.object;

import cn.hutool.http.ContentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 工具链平台HTTP请求对象。
 *
 * @author laiqi
 * @since 2024/9/6
 */
@ApiModel("工具链平台HTTP请求对象")
@Setter
@Getter
public class ToolChainHttpRequest implements Serializable {

    /**
     * URL参数
     */
    @ApiModelProperty(value = "URL参数")
    private Map<String, Object> queryParams;

    /**
     * 请求头
     */
    @ApiModelProperty(value = "请求头")
    private Map<String, List<String>> headers;

    /**
     * 请求体。与请求体类型有关。当contentType = application/json，则请求体为JSON格式。当contentType = application/x-www-form-urlencoded，则请求体为表单格式。
     */
    @ApiModelProperty(value = "请求体")
    private String body;

    /**
     * 请求体类型。默认是JSON格式。如需表单请求，请设置此参数为表单格式。
     */
    @ApiModelProperty(value = "请求体类型。默认为application/json", example = "application/x-www-form-urlencoded")
    private String contentType = ContentType.JSON.getValue();
}