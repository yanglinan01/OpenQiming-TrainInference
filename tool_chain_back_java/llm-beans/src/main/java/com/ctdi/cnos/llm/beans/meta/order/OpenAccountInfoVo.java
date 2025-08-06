package com.ctdi.cnos.llm.beans.meta.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2024/12/18 15:09
 * @Description
 */
@Data
@ApiModel(description = "待开通用户")
public class OpenAccountInfoVo implements Serializable {

    private static final long serialVersionUID = -1L;


    /**
     * 工单id
     */
    @ApiModelProperty(value = "工单id")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal orderId;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String toolChainName;

    /**
     * 人力账号
     */
    @ApiModelProperty(value = "人力账号")
    private String toolChainOAName;

    /**
     * 省份/公司
     */
    @ApiModelProperty(value = "省份/公司")
    private String provinceCompany;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String toolChainPhone;

    @ApiModelProperty(value = "训推工具链是否开通,字典YSE_OR_NO（0是，1否）")
    private String toolAuth;

    @ApiModelProperty(value = "智能体工具链是否开通,字典YSE_OR_NO（0是，1否）")
    private String agentAuth;

    @ApiModelProperty(value = "训推工具链开通需求  0：是 1：否 字典YES_OR_NO")
    private String toolPermissionState;

    @ApiModelProperty(value = "智能体工具链开通需求  0：是 1：否 字典YES_OR_NO")
    private String agentPermissionState;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;
}
