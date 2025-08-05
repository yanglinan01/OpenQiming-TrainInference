package com.ctdi.cnos.llm.beans.meta.order;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("meta.mm_order")
@ApiModel(value = "Order对象", description = "")
public class Order implements Serializable {
    private static final long serialVersionUID = -1L;
    /**
     * id
     */
	@ApiModelProperty(value = "id", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id")
    private BigDecimal id;

    /**
     * 工单标题
     */
	@ApiModelProperty(value = "工单标题", required = true)
    @TableField("order_title")
    private String orderTitle;

    /**
     * 工单号
     */
    @ApiModelProperty(value = "工单号")
    @TableField("order_code")
    private String orderCode;

    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳")
    @TableField("timestamp")
    private String timestamp;

    /**
     * 工单发起人姓名
     */
    @ApiModelProperty(value = "工单发起人姓名")
    @TableField("create_oper_name")
    private String createOperName;

    /**
     * 工单发起人部门
     */
    @ApiModelProperty(value = "工单发起人部门")
    @TableField("create_oper_org_name")
    private String createOperOrgName;

    /**
     * 工单发起人电话
     */
    @ApiModelProperty(value = "工单发起人电话")
    @TableField("create_oper_phone")
    private String createOperPhone;

    /**
     * 涉及系统	训推工具链：1	智能体工具链：2	训推工具链,智能体工具链：1,2	传编码，例：1	
     */
    @ApiModelProperty(value = "涉及系统	训推工具链：1	智能体工具链：2	训推工具链,智能体工具链：1,2	传编码，例：1	")
    @TableField("reference_system")
    private String referenceSystem;

    /**
     * 是否关联需求工单	是：1	否：2	传编码，例：1
     */
    @ApiModelProperty(value = "是否关联需求工单	是：1	否：2	传编码，例：1")
    @TableField("is_requirement")
    private String isRequirement;

    /**
     * 需求工单号	当‘是否关联需求工单’为1是，此项必填
     */
    @ApiModelProperty(value = "需求工单号	当‘是否关联需求工单’为1是，此项必填")
    @TableField("requirement_number")
    private String requirementNumber;

    /**
     * 紧急程度	紧急：1	普通：2	传编码，例：1
     */
    @ApiModelProperty(value = "紧急程度	紧急：1	普通：2	传编码，例：1")
    @TableField("degree_urgency")
    private String degreeUrgency;

    /**
     * 省份/公司
     */
    @ApiModelProperty(value = "省份/公司")
    @TableField("requesting_area")
    private String requestingArea;

    /**
     * 需求提出部门
     */
    @ApiModelProperty(value = "需求提出部门")
    @TableField("requesting_department")
    private String requestingDepartment;

    /**
     * 需求联系人
     */
    @ApiModelProperty(value = "需求联系人")
    @TableField("demand_contact")
    private String demandContact;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    @TableField("demand_contact_phone")
    private String demandContactPhone;

    /**
     * 预计覆盖人数
     */
    @ApiModelProperty(value = "预计覆盖人数")
    @TableField("cover_people")
    private String coverPeople;

    /**
     * 预计月调用量
     */
    @ApiModelProperty(value = "预计月调用量")
    @TableField("expected_transfer_volume")
    private String expectedTransferVolume;

    /**
     * 需求概述
     */
    @ApiModelProperty(value = "需求概述")
    @TableField("requirements_overview")
    private String requirementsOverview;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "审核状态：1：审核中 2：审核结束")
    @TableField("review_status")
    private String reviewStatus;

    @ApiModelProperty(value = "审核时间", example = "2024-07-02 16:49:53.950")
    @TableField(value = "review_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date reviewTime;

    @ApiModelProperty(value = "通开账号列表")
    @TableField(exist = false)
    private List<OrderUser> accountInfoList;

    @ApiModelProperty(value = "处理流水列表")
    @TableField(exist = false)
    private List<OrderNode> flowProcessingList;

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
     * 是否上传门户 0是，1否 字典YES_OR_NO
     */
    @ApiModelProperty(value = "是否上传门户 0是，1否 字典YES_OR_NO")
    @TableField("upload_status")
    private String uploadStatus;

}