package com.ctdi.cnos.llm.beans.meta.order;

import com.ctdi.cnos.llm.base.object.Groups;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *  DTO对象。
 *
 * @author xuwj09
 * @since 2024/12/06
 */
@ApiModel(description = "OrderUserDTO对象")
@Data
public class OrderUserDTO implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * id
     */
	@ApiModelProperty(value = "id", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
	@NotNull(message = "数据验证失败，id不能为空！", groups = {Groups.UPDATE.class})
	private BigDecimal id;
	
	private String orderCode;
	
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
	
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
	private String remark;

    @ApiModelProperty(value = "人员是否通过审核 0：是 1：否 字典YES_OR_NO")
    private String passStatus;

    @ApiModelProperty(value = "审核通过时间", example = "2024-07-02 16:49:53")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date passTime;

    /**
     * 创建者Id。
     */
    @ApiModelProperty(value = "创建者Id", example = "0")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal creatorId;

    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "创建时间", example = "2024-07-02 16:49:53")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    /**
     * 更新者Id。
     */
    @ApiModelProperty(value = "更新者Id", example = "0")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal modifierId;

    /**
     * 更新时间。
     */
    @ApiModelProperty(value = "创建时间", example = "2024-07-02 16:49:53")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date modifyDate;

    /**
     * 工单id
     */
    @ApiModelProperty(value = "工单id")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal orderId;
}