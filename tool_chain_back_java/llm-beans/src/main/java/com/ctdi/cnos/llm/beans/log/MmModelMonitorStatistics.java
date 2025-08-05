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

/**
 * @author caojunhao
 * @DATE 2024/7/4
 */
@Data
@ApiModel("日志中心-模型监控日志概括")
@TableName("log.mm_model_monitor_statistics")
public class MmModelMonitorStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("训练ID")
    @TableField("task_id")
    private Long taskId;

    @ApiModelProperty("调用总token数")
    @TableField("token_total")
    private Long tokenTotal;

    @ApiModelProperty("输入token数")
    @TableField("token_input")
    private Long tokenInput;

    @ApiModelProperty("输出token数")
    @TableField("token_output")
    private Long tokenOutput;

    @ApiModelProperty("调用接口总数")
    @TableField("intf_total")
    private Long intfTotal;
}
