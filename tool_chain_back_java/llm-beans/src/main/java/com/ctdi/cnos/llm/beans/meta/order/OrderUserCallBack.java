package com.ctdi.cnos.llm.beans.meta.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *  DTO对象。
 *
 * @author wangyy
 * @since 2024/12/19
 */
@ApiModel(description = "OrderUserCallBack对象")
@Data
public class OrderUserCallBack implements Serializable {
    private static final long serialVersionUID = -1L;

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

    /**
     * 工具链权限状态	开通权限：1	关闭权限：2	账号异常：3
     */
    @ApiModelProperty(value = "工具链权限状态	开通权限：1	关闭权限：2	账号异常：3")
	private String permissionState;

}
