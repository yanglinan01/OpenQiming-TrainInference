package com.ctdi.cnos.llm.beans.meta.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *  DTO对象。
 *
 * @author xuwj09
 * @since 2024/12/06
 */
@ApiModel(description = "OrderNodeCallBack对象")
@Data
public class OrderNodeCallBack implements Serializable {

    private static final long serialVersionUID = -1L;

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

}
