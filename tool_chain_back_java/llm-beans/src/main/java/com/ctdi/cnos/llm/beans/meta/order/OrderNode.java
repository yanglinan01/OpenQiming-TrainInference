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

/**
 *  实体对象。
 *
 * @author xuwj09
 * @since 2024/12/06
 */
@Getter
@Setter
@TableName("meta.mm_order_node")
@ApiModel(value = "OrderNode对象", description = "")
public class OrderNode implements Serializable {

    private static final long serialVersionUID = -1L;
    /**
     * id
     */
	@ApiModelProperty(value = "id", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id")
    private BigDecimal id;

    /**
     * 环节编码
     */
    @ApiModelProperty(value = "环节编码")
    @TableField("order_code")
    private String orderCode;

    /**
     * 环节编码
     */
    @ApiModelProperty(value = "环节编码")
    @TableField("node_code")
    private String nodeCode;

    /**
     * 环节名称
     */
    @ApiModelProperty(value = "环节名称")
    @TableField("node_name")
    private String nodeName;

    /**
     * 待处理人id	门户主数据对应的用户账号和名称，多个人逗号(,)分割
     */
    @ApiModelProperty(value = "待处理人id	门户主数据对应的用户账号和名称，多个人逗号(,)分割")
    @TableField("node_todo_staff_id")
    private String nodeTodoStaffId;

    /**
     * 待处理人姓名
     */
    @ApiModelProperty(value = "待处理人姓名")
    @TableField("node_todo_staff_name")
    private String nodeTodoStaffName;

    /**
     * 当前环节开始时间	精确到秒，日期格式“YYYY-MM-DD HH:MI:SS”
     */
    @ApiModelProperty(value = "当前环节开始时间	精确到秒，日期格式“YYYY-MM-DD HH:MI:SS”")
    @TableField("node_start_time")
    private String nodeStartTime;

    /**
     * 环节状态	环节处理状态：	0 同意	1 不同意
     */
    @ApiModelProperty(value = "环节状态	环节处理状态：	0 同意	1 不同意")
    @TableField("node_status")
    private String nodeStatus;

    /**
     * 实际处理人id
     */
    @ApiModelProperty(value = "实际处理人id")
    @TableField("node_do_staff_id")
    private String nodeDoStaffId;

    /**
     * 实际处理人姓名
     */
    @ApiModelProperty(value = "实际处理人姓名")
    @TableField("node_do_staff_name")
    private String nodeDoStaffName;

    /**
     * 环节实际完成时间	精确到秒，日期格式“YYYY-MM-DD HH:MI:SS”
     */
    @ApiModelProperty(value = "环节实际完成时间	精确到秒，日期格式“YYYY-MM-DD HH:MI:SS”")
    @TableField("node_end_time")
    private String nodeEndTime;

    /**
     * 环节处理意见
     */
    @ApiModelProperty(value = "环节处理意见")
    @TableField("node_opinions")
    private String nodeOpinions;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

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