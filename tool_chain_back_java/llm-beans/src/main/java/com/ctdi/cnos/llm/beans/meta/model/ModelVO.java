/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 模型
 *
 * @author huangjinhua
 * @since 2024/7/1
 */
@Data
@ToString(callSuper = true)
@ApiModel("模型VO")
public class ModelVO extends Model {

    @ApiModelProperty(value = "训练方法，多个逗号分隔，详见字典TRAIN_TASK_METHOD", example = "1")
    private String trainMethod;

    @ApiModelProperty(value = "训练类型，详见字典MODEL_TRAIN_TYPE", example = "SFT")
    private String trainType;
    /**
     * 训练方法字典列表
     */
    @ApiModelProperty("训练方法列表")
    private List<String> trainMethodList;

    @ApiModelProperty(value = "前端是否选中（0是，1否）", example = "1")
    private String isActivate = "1";

    @ApiModelProperty(value = "页大小，分页查询", example = "1")
    private Integer pageSize = 20;

    @ApiModelProperty(value = "当前页，分页查询", example = "1")
    private Integer currentPage = 1;

    @ApiModelProperty(value = "是否用户权限（0是，1否）", example = "1")
    private String userScope;

    @ApiModelProperty(value = "当前用户Id", example = "1")
    @JsonIgnore
    private Long currentUserId;

    @ApiModelProperty(value = "类型名称", example = "大预言模型")
    private String typeName;

    @ApiModelProperty(value = "可用用途，字典MODEL_AUTH_USAGE", example = "1")
    private String enableUsage;

    @ApiModelProperty(value = "归属名称", example = "系统模型")
    private String belongName;

    @ApiModelProperty(value = "区域名称", example = "集团")
    private String regionName;
}