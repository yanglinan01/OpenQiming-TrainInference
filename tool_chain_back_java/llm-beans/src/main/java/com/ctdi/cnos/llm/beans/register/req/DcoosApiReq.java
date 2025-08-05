package com.ctdi.cnos.llm.beans.register.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangxue
 * @description
 * @data 2024/4/17 15:13
 */
@Data
@SuppressWarnings("all")
public class DcoosApiReq {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private String id;

    /**
     * 接口名称
     */
    @ApiModelProperty(value = "接口名称")
    private String name;

    /**
     * 接口描述
     */
    @ApiModelProperty(value = "接口描述")
    private String description;

    /**
     * 请求方式
     */
    @ApiModelProperty(value = "请求方式")
    private String methodType;

    /**
     * 接口url
     */
    @ApiModelProperty(value = "接口url")
    private String apiUrl;

    /**
     * 入参
     */
    @ApiModelProperty(value = "入参")
    private String requestParams;

    /**
     * 回参
     */
    @ApiModelProperty(value = "回参")
    private String responseParams;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String creatorId;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String modifierId;

    /**
     * 是否有效
     */
    @ApiModelProperty(value = "是否有效")
    private String isValid;

    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private String isDelete;

    /**
     * 服务类型
     */
    @ApiModelProperty(value = "服务类型(http,webService)")
    private String serviceType;

}
