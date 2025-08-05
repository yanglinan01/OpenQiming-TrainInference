package com.ctdi.cnos.llm.beans.meta.order;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctdi.cnos.llm.base.annotation.UserFilterColumn;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *  实体对象。
 *
 * @author xuwj09
 * @since 2024/12/06
 */
@Getter
@Setter
@TableName("meta.mm_order_user")
@ApiModel(value = "OrderUser对象", description = "")
public class OrderUser implements Serializable {

    private static final long serialVersionUID = -1L;
    /**
     * id
     */
	@ApiModelProperty(value = "id", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id")
    private BigDecimal id;

    @TableField("order_code")
    private String orderCode;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    @TableField("tool_chain_name")
    private String toolChainName;

    /**
     * 人力账号
     */
    @ApiModelProperty(value = "人力账号")
    @TableField("tool_chain_oa_name")
    private String toolChainOAName;

    /**
     * 省份/公司
     */
    @ApiModelProperty(value = "省份/公司")
    @TableField("province_company")
    private String provinceCompany;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    @TableField("tool_chain_phone")
    private String toolChainPhone;

    /**
     * 工具链权限状态	开通权限：1	关闭权限：2	账号异常：3
     */
    @ApiModelProperty(value = "工具链权限状态	开通权限：1	关闭权限：2	账号异常：3")
    @TableField("permission_state")
    private String permissionState;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "人员是否通过审核 0：是 1：否")
    @TableField("pass_status")
    private String passStatus;

    @ApiModelProperty(value = "审核通过时间", example = "2024-07-02 16:49:53.950")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "pass_time")
    private Date passTime;

    /**
     * 创建者Id。
     */
    @UserFilterColumn
    @ApiModelProperty(value = "创建者Id", example = "0")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private BigDecimal creatorId;

    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "创建时间", example = "2024-07-02 16:49:53")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 更新者Id。
     */
    @ApiModelProperty(value = "更新者Id", example = "0")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "modifier_id", fill = FieldFill.INSERT_UPDATE)
    private BigDecimal modifierId;

    /**
     * 更新时间。
     */
    @ApiModelProperty(value = "创建时间", example = "2024-07-02 16:49:53")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "modify_date", fill = FieldFill.INSERT_UPDATE)
    private Date modifyDate;

    /**
     * 工单id
     */
    @ApiModelProperty(value = "工单id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "order_id")
    private BigDecimal orderId;
}