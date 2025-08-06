package com.ctdi.cnos.llm.beans.job;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务调度日志表 mm_job_log
 *
 * @author huangjinhua
 * @since 2024/6/3
 */
@Setter
@Getter
@ApiModel("定时任务调度日志")
@TableName("job.mm_job_log")
public class MmJobLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "ID，修改时必填!", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "任务ID", required = true, example = "1775389563611901951")
    @TableField("job_id")
    private Long jobId;
    @ApiModelProperty(value = "任务名称", required = true, example = "样例单参数")
    @TableField("job_name")
    private String jobName;

    @ApiModelProperty(value = "任务组名", required = true, example = "DEFAULT")
    @TableField("job_group")
    private String jobGroup;

    @ApiModelProperty(value = "调用目标字符串", required = true, example = "demoJob.params(\"字符串\")")
    @TableField("invoke_target")
    private String invokeTarget;
    @ApiModelProperty(value = "日志信息", required = true, example = "")
    @TableField("job_message")
    private String jobMessage;

    @ApiModelProperty(value = "执行状态（0正常 1失败）", required = true, example = "0")
    @TableField("status")
    private String status;
    @ApiModelProperty(value = "异常信息", required = true, example = "")
    @TableField("exception_info")
    private String exceptionInfo;

    @ApiModelProperty(value = "创建时间", required = true, example = "2024-04-01 08:00:00", hidden = true)
    @TableField("create_date")
    private Date createDate;

}
