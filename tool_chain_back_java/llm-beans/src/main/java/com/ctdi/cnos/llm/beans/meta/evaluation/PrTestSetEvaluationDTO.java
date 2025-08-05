package com.ctdi.cnos.llm.beans.meta.evaluation;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ctdi.cnos.llm.base.object.Groups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 问答对测试数据集评估 Dto对象。
 *
 * @author laiqi
 * @since 2024/09/03
 */
@ApiModel(description = "PrTestSetEvaluationDto对象")
@Data
public class PrTestSetEvaluationDTO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "数据验证失败，主键不能为空！", groups = {Groups.UPDATE.class})
    private Long id;

    /**
     * 数据集ID
     */
    @ApiModelProperty(value = "数据集ID")
    @NotNull(message = "数据验证失败，数据集ID不能为空！", groups = {Groups.ADD.class})
    private Long dataSetId;

    /**
     * 模型任务ID
     */
    @ApiModelProperty(value = "模型任务ID")
    @NotNull(message = "数据验证失败，模型任务ID不能为空！", groups = {Groups.PAGE.class, Groups.QUERY.class, Groups.ADD.class})
    private Long modelTaskId;

    /**
     * 评估状态
     */
    @ApiModelProperty(value = "评估状态")
    private String status;

    /**
     * 部署任务状态
     */
    @ApiModelProperty(value = "部署任务状态")
    private String deployStatus;
    /**
     * 部署任务目标位置
     */
    @ApiModelProperty(value = "部署任务目标位置")
    private String deployTarget;

    /**
     * 部署任务推理url
     */
    @ApiModelProperty(value = "部署任务推理url")
    private String deployUrl;
    /**
     * 部署完成时间
     */
    @ApiModelProperty(value = "")
    private Date deployFinishDate;
    /**
     * 评估类型(0强化; 1普通)
     */
    @ApiModelProperty(value = "评估类型(0强化; 1普通)")
    private String type;

    /**
     * 是否构建强化学习
     */
    @ApiModelProperty(value = "是否构建强化学习")
    private Boolean built;

    /**
     * 温度
     */
    @ApiModelProperty(value = "温度")
    private Float temperature;

    /**
     * max_tokens
     */
    @ApiModelProperty(value = "max_tokens")
    private Integer maxTokens;

    /**
     * send_status
     */
    @TableField("send_status")
    private String sendStatus;

}