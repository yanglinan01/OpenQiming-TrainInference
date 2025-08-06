package com.ctdi.cnos.llm.beans.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author HuangGuanSheng
 * @date 2024-07-04 11:06
 */
@Data
@ApiModel("日志中心-接口调用统计")
@TableName("log.mm_model_monitor_intf")
public class MmModelMonitorIntf implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("训练任务id")
    @TableField("task_id")
    private Long taskId;

    @ApiModelProperty("接口调用状态;0:成功,1:失败")
    @TableField("intf_call_type")
    private Integer intfCallType;

    @ApiModelProperty("接口调用时间")
    @TableField("intf_call_date")
    private Date intfCallDate;

    @ApiModelProperty("接口调用时间-日期")
    @TableField("intf_call_date_days")
    private Integer intfCallDateDays;

    @ApiModelProperty("接口调用时间-小时")
    @TableField("intf_call_date_hours")
    private Integer intfCallDateHours;

    @ApiModelProperty("接口调用次数")
    @TableField("intf_call_counts")
    private Long intfCallCounts;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

}
