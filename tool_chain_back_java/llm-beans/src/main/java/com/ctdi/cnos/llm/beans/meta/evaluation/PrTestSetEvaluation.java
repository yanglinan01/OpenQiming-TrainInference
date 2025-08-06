package com.ctdi.cnos.llm.beans.meta.evaluation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctdi.cnos.llm.base.annotation.RelationGlobalDict;
import com.ctdi.cnos.llm.base.object.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * 问答对测试数据集评估 实体对象。
 *
 * @author laiqi
 * @since 2024/09/03
 */
@Getter
@Setter
@TableName("meta.mm_pr_test_set_evaluation")
@ApiModel(value = "PrTestSetEvaluation对象", description = "问答对测试数据集评估")
public class PrTestSetEvaluation extends BaseModel {

    @ApiModelProperty(value = "主键", required = true)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "数据集ID")
    @TableField("data_set_id")
    private Long dataSetId;

    @ApiModelProperty(value = "模型任务ID")
    @TableField("model_task_id")
    private Long modelTaskId;

    @ApiModelProperty(value = "评估状态")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "部署任务状态")
    @TableField("deploy_status")
    private String deployStatus;
    @ApiModelProperty(value = "部署任务目标位置")
    @TableField("deploy_target")
    private String deployTarget;

    @ApiModelProperty(value = "部署任务推理url")
    @TableField("deploy_url")
    private String deployUrl;

    @ApiModelProperty(value = "部署完成时间")
    @TableField("deploy_finish_date")
    private Date deployFinishDate;

    @ApiModelProperty(value = "失败原因")
    @TableField("result")
    private String result;

    @ApiModelProperty(value = "评估类型(0强化; 1普通)")
    @TableField("type")
    private String type;

    @RelationGlobalDict(masterIdField = "status", dictCode = "DATA_SET_EVALUATION")
    @ApiModelProperty(value = "评估状态字典")
    @TableField(exist = false)
    private Map<String, String> statusDictMap;

    @RelationGlobalDict(masterIdField = "type", dictCode = "DATA_SET_EVALUATION")
    @ApiModelProperty(value = "评估类型字典")
    @TableField(exist = false)
    private Map<String, String> typeDictMap;

    @ApiModelProperty(value = "是否构建强化学习")
    @TableField("is_built")
    private Boolean built;

    @ApiModelProperty(value = "温度")
    @TableField("temperature")
    private Float temperature;

    @ApiModelProperty(value = "max_tokens")
    @TableField("max_tokens")
    private Integer maxTokens;

    @ApiModelProperty(value = "send_status")
    @TableField("send_status")
    private String sendStatus;

}