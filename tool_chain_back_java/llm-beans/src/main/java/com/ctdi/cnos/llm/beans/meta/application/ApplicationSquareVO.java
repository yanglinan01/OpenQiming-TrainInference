/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.application;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 应用广场
 *
 * @author huangjinhua
 * @since 2024/6/11
 */
@Getter
@Setter
@ToString
@ApiModel("应用广场 vo类")
@Accessors(chain = true)
public class ApplicationSquareVO extends ApplicationSquare {

    @ApiModelProperty(value = "应用类型名称", example = "启明大模型")
    private String typeName;
    @ApiModelProperty(value = "场景名称", example = "规章制度知识助手")
    private String sceneName;
    @ApiModelProperty(value = "前端是否选中（0是，1否）", example = "1")
    private String isActivate = "1";
}
