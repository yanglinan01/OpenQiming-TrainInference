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
@ApiModel(description = "OrderNodeDTO对象")
@Data
public class OrderNodeDTO implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * id
     */
	@ApiModelProperty(value = "id", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
	@NotNull(message = "数据验证失败，id不能为空！", groups = {Groups.UPDATE.class})
	private BigDecimal id;
	
    /**
     * 环节编码
     */
    @ApiModelProperty(value = "环节编码")
	private String orderCode;
	
    /**
     * 环节编码
     */
    @ApiModelProperty(value = "环节编码")
	private String nodeCode;
	
    /**
     * 环节名称
     */
    @ApiModelProperty(value = "环节名称")
	private String nodeName;
	
    /**
     * 待处理人id	门户主数据对应的用户账号和名称，多个人逗号(,)分割
     */
    @ApiModelProperty(value = "待处理人id	门户主数据对应的用户账号和名称，多个人逗号(,)分割")
	private String nodeTodoStaffId;
	
    /**
     * 待处理人姓名
     */
    @ApiModelProperty(value = "待处理人姓名")
	private String nodeTodoStaffName;
	
    /**
     * 当前环节开始时间	精确到秒，日期格式“YYYY-MM-DD HH:MI:SS”
     */
    @ApiModelProperty(value = "当前环节开始时间	精确到秒，日期格式“YYYY-MM-DD HH:MI:SS”")
	private String nodeStartTime;
	
    /**
     * 环节状态	环节处理状态：	0 同意	1 不同意
     */
    @ApiModelProperty(value = "环节状态	环节处理状态：	0 同意	1 不同意")
	private String nodeStatus;
	
    /**
     * 实际处理人id
     */
    @ApiModelProperty(value = "实际处理人id")
	private String nodeDoStaffId;
	
    /**
     * 实际处理人姓名
     */
    @ApiModelProperty(value = "实际处理人姓名")
	private String nodeDoStaffName;
	
    /**
     * 环节实际完成时间	精确到秒，日期格式“YYYY-MM-DD HH:MI:SS”
     */
    @ApiModelProperty(value = "环节实际完成时间	精确到秒，日期格式“YYYY-MM-DD HH:MI:SS”")
	private String nodeEndTime;
	
    /**
     * 环节处理意见
     */
    @ApiModelProperty(value = "环节处理意见")
	private String nodeOpinions;
	
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
	private String remark;

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