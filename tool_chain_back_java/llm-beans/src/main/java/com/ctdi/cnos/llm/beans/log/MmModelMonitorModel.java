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
 * @author caojunhao
 * @DATE 2024/7/4
 */
@Data
@ApiModel("日志中心-模型调用统计")
@TableName("log.mm_model_monitor_model")
public class MmModelMonitorModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("训练任务id")
    @TableField("task_id")
    private Long taskId;

    @ApiModelProperty("模型调用时间")
    @TableField("model_call_date")
    private Date modelCallDate;

    @ApiModelProperty("模型调用时间-日期")
    @TableField("model_call_date_days")
    private Integer modelCallDateDays;

    @ApiModelProperty("模型调用时间-小时")
    @TableField("model_call_date_hours")
    private Integer modelCallDateHours;

    @ApiModelProperty("模型调用token数")
    @TableField("model_output_token")
    private Long modelOutputToken;

    @ApiModelProperty("模型调用token数")
    @TableField("model_input_token")
    private Long modelInputToken;
}
