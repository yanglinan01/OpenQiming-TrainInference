/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.operationCenter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 运营中心概览
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@Data
@ApiModel("运营中心概览VO类")
@Accessors(chain = true)
public class OperationCenterStatVO {
    /**
     * 平台使用量
     */
    @ApiModelProperty(value = "平台使用量", example = "0")
    private Long platformUsedCount;


    /**
     * 平台用户量
     */
    @ApiModelProperty(value = "平台用户量", example = "0")
    private Long platformUserCount;

    /**
     * 覆盖公司数
     */
    @ApiModelProperty(value = "覆盖公司数", example = "0")
    private Long companyCount;


    /**
     * 平台模型数
     */
    @ApiModelProperty(value = "平台模型数", example = "0")
    private Long modelCount;


    /**
     * 平台应用数
     */
    @ApiModelProperty(value = "平台应用数", example = "0")
    private Long agentCount;

    /**
     * 平台应用数
     */
    @ApiModelProperty(value = "平台工作流数", example = "0")
    private Long workflowCount;


    /**
     * 注册接口数
     */
    @ApiModelProperty(value = "注册接口数", example = "0")
    private Long apiCount;
}
