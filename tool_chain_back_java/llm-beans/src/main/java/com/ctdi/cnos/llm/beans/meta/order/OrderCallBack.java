package com.ctdi.cnos.llm.beans.meta.order;

import com.ctdi.cnos.llm.base.object.Groups;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *  DTO对象。
 *
 * @author wangyy
 * @since 2024/12/19
 */
@ApiModel(description = "OrderCallBack对象")
@Data
public class OrderCallBack implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * id
     */
	@ApiModelProperty(value = "id", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
	@NotNull(message = "数据验证失败，id不能为空！", groups = {Groups.UPDATE.class})
	private BigDecimal id;

    /**
     * 工单标题
     */
	@ApiModelProperty(value = "工单标题", required = true)
	@NotBlank(message = "数据验证失败，工单标题不能为空！")
	private String orderTitle;

    /**
     * 工单号
     */
    @ApiModelProperty(value = "工单号")
	private String orderCode;

    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳")
	private String timestamp;

    /**
     * 工单发起人姓名
     */
    @ApiModelProperty(value = "工单发起人姓名")
	private String createOperName;

    /**
     * 工单发起人部门
     */
    @ApiModelProperty(value = "工单发起人部门")
	private String createOperOrgName;

    /**
     * 工单发起人电话
     */
    @ApiModelProperty(value = "工单发起人电话")
	private String createOperPhone;

    /**
     * 涉及系统	训推工具链：1	智能体工具链：2	训推工具链,智能体工具链：1,2	传编码，例：1
     */
    @ApiModelProperty(value = "涉及系统	训推工具链：1	智能体工具链：2	训推工具链,智能体工具链：1,2	传编码，例：1	")
	private String referenceSystem;

    /**
     * 是否关联需求工单	是：1	否：2	传编码，例：1
     */
    @ApiModelProperty(value = "是否关联需求工单	是：1	否：2	传编码，例：1")
	private String isRequirement;

    /**
     * 需求工单号	当‘是否关联需求工单’为1是，此项必填
     */
    @ApiModelProperty(value = "需求工单号	当‘是否关联需求工单’为1是，此项必填")
	private String requirementNumber;

    /**
     * 紧急程度	紧急：1	普通：2	传编码，例：1
     */
    @ApiModelProperty(value = "紧急程度	紧急：1	普通：2	传编码，例：1")
	private String degreeUrgency;

    /**
     * 省份/公司
     */
    @ApiModelProperty(value = "省份/公司")
	private String requestingArea;

    /**
     * 需求提出部门
     */
    @ApiModelProperty(value = "需求提出部门")
	private String requestingDepartment;

    /**
     * 需求联系人
     */
    @ApiModelProperty(value = "需求联系人")
	private String demandContact;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
	private String demandContactPhone;

    /**
     * 预计覆盖人数
     */
    @ApiModelProperty(value = "预计覆盖人数")
	private String coverPeople;

    /**
     * 预计月调用量
     */
    @ApiModelProperty(value = "预计月调用量")
	private String expectedTransferVolume;

    /**
     * 需求概述
     */
    @ApiModelProperty(value = "需求概述")
	private String requirementsOverview;

    @ApiModelProperty(value = "通开账号列表")
    private List<OrderUserCallBack> accountInfoList;

    @ApiModelProperty(value = "处理流水列表")
    private List<OrderNodeCallBack> flowProcessingList;

}
