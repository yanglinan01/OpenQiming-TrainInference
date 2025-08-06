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
 * 定时任务调度表 mm_job
 *
 * @author huangjinhua
 * @since 2024/6/3
 */
@Setter
@Getter
@ApiModel("定时任务调度")
@TableName("job.mm_job")
public class MmJob implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务ID，修改时必填!", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "任务名称", required = true, example = "样例单参数")
    @TableField("job_name")
    private String jobName;

    @ApiModelProperty(value = "任务组名", required = true, example = "DEFAULT")
    @TableField("job_group")
    private String jobGroup;

    @ApiModelProperty(value = "调用目标字符串", required = true, example = "demoJob.params(\"字符串\")")
    @TableField("invoke_target")
    private String invokeTarget;

    @ApiModelProperty(value = "cron执行表达式", required = true, example = "* 0/30 * * * ?")
    @TableField("cron_expression")
    private String cronExpression;

    @ApiModelProperty(value = "计划执行错误策略（0默认 1立即执行 2执行一次 3放弃执行））", required = true, example = "0")
    @TableField("misfire_policy")
    private String misfirePolicy;

    @ApiModelProperty(value = "是否并发执行（0允许 1禁止）", required = true, example = "1")
    @TableField("concurrent")
    private String concurrent;

    @ApiModelProperty(value = "任务状态（0正常 1暂停）", required = true, example = "1")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "创建人", required = true, example = "-1", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("creator_id")
    private Long creatorId;

    @ApiModelProperty(value = "创建时间", required = true, example = "2024-04-01 08:00:00", hidden = true)
    @TableField("create_date")
    private Date createDate;

    @ApiModelProperty(value = "更新人", required = true, example = "-1", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("modifier_id")
    private Long modifierId;

    @ApiModelProperty(value = "更新时间", required = true, example = "2024-04-01 08:10:00", hidden = true)
    @TableField("modify_date")
    private Date modifyDate;

    @ApiModelProperty(value = "备注", example = "xxxx", hidden = true)
    @TableField("remark")
    private String remark;

}