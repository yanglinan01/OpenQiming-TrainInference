package com.ctdi.cnos.llm.beans.register;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author zhangxue
 * @data 2024/4/17 14:52
 */

/**
 * 接口信息表
 */

@Data
@ApiModel("DcoosApi实体类")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DcoosApi {
    private static final long serialVersionUID = -91111055207380488L;
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private String id;
    /**
     * 接口名称
     */
    @ApiModelProperty(value = "接口名称", example = "prompt新增接口")
    private String name;
    /**
     * 接口描述
     */
    @ApiModelProperty(value = "接口描述", example = "集团大模型prompt新增接口")
    private String description;
    /**
     * 请求方式
     */
    @ApiModelProperty(value = "请求方式", example = "post")
    private String methodType;
    /**
     * 接口url
     */
    @ApiModelProperty(value = "接口url", example = "/metadata-service/prompt/add")
    private String apiUrl;
    /**
     * 入参
     */
    @ApiModelProperty(value = "入参", example = "json")
    private String requestParams;
    /**
     * 回参
     */
    @ApiModelProperty(value = "回参", example = "json")
    private String responseParams;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", example = "1")
    private String status;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", example = "-1")
    private String creatorId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", example = "2024-04-17 14:00:33.026")
    private Date createDate;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人", example = "-1")
    private String modifierId;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", example = "2024-04-17 14:00:33.026")
    private Date modifyDate;
    /**
     * 是否合理
     */
    @ApiModelProperty(value = "是否有效是否有效(数据字典:是10050001;10050001否)", example = "10050001")
    private String isValid;
    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除(数据字典:是10050001;10050001否)", example = "10050002")
    private String isDelete;

    /**
     * 服务类型(http,webService)
     */
    @ApiModelProperty(value = "服务类型(http,webService)", example = "http")
    private String serviceType;

    /**
     * dcossUrl(服务域名/服务类型/guid)
     */
    @ApiModelProperty(value = "dcossUrl(服务域名/服务类型/guid)", example = "http://localhost:8801/llm-web/web/dcoosApi//queryById/4cc59a53a1034312a2a19afce72d6333")
    private String dcossApiUrl;
}

