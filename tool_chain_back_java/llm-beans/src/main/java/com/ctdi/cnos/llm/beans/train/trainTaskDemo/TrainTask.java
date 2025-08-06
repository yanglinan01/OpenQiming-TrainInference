/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.train.trainTaskDemo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctdi.cnos.llm.base.object.BaseModel;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskParamVO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 训练任务 模拟demo
 *
 * @author huangjinhua
 * @since 2024/9/20
 */
@Data
@ApiModel("训练任务demo")
@TableName("train.mm_train_task_demo")
public class TrainTask extends BaseModel {
    @ApiModelProperty(value = "ID，修改时必填!", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId("id")
    private Long id;
    @ApiModelProperty(value = "名称", required = true, example = "训练作业")
    @TableField("name")
    private String name;
    @ApiModelProperty(value = "模型ID，meta.mm_model.id", example = "1775389563611901951")
    @TableField("model_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modelId;
    @ApiModelProperty(value = "训练方法，字典TRAIN_TASK_METHOD", example = "1")
    @TableField("method")
    private String method;
    @ApiModelProperty(value = "训练类型，字典MODEL_TRAIN_TYPE", example = "1")
    @TableField("\"type\"")
    private String type;
    @ApiModelProperty(value = "数据集ID,meta.mm_data_set.id", example = "1")
    @TableField("data_set_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dataSetId;
    @ApiModelProperty(value = "超参配置", example = "")
    @TableField(exist = false)
    private List<TrainTaskParamVO> param;
    @ApiModelProperty(value = "超参配置，用于数据库存储", example = "")
    @TableField("param")
    private String paramStr;

    @ApiModelProperty(value = "任务状态，字典TRAIN_TASK_STATUS", example = "")
    @TableField("status")
    private String status;


    @ApiModelProperty(value = "训练接口目标，GZ：贵州，QD青岛，mate.mm_cluster.code", example = "GZ")
    @TableField("train_target")
    private String trainTarget;

    @ApiModelProperty(value = "是否已提交到k8s队列，0是，1否 字典YES_OR_NO", example = "1")
    @TableField("submit_status")
    private String submitStatus;

    @ApiModelProperty(value = "省份名称", example = "集团")
    @TableField("region_name")
    private String regionName;

    @ApiModelProperty(value = "总迭代次数", example = "1000")
    @TableField("iterate_total")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long iterateTotal;
    @ApiModelProperty(value = "当前迭代次数", example = "70")
    @TableField("iterate_curr")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long iterateCurr;

    @ApiModelProperty(value = "已运行时间", example = "10分30秒")
    @TableField("runtime")
    private String runtime;
    @ApiModelProperty(value = "预计剩余训练时间", example = "1分30秒")
    @TableField("remain_time")
    private String remainTime;
    @ApiModelProperty(value = "训练结果信息", example = "Resource limit reached")
    @TableField("result")
    private String result;

    @ApiModelProperty(value = "区域编码", example = "-1")
    @TableField("region_code")
    private String regionCode;

    @ApiModelProperty(value = "训练Loss数据(有序loss值按,分隔)", example = "100, 200, 300, 222, 333")
    @TableField("training_loss")
    private String trainingLoss;

    @ApiModelProperty(value = "Loss趋势分析", example = "模型正在有效学习，并且能够在训练集外的数据上表现良好。")
    @TableField("loss_trend")
    private String lossTrend;

    @ApiModelProperty(value = "集群ID，数据来源 算网调度AI-智算平台接口，id字段", example = "2341")
    @TableField("ai_cluster_id")
    private String aiClusterId;

    @ApiModelProperty(value = "集群名称，数据来源 算网调度AI-智算平台接口,title字段", example = "2341")
    @TableField("ai_cluster_name")
    private String aiClusterName;

    @ApiModelProperty(value = "智算任务工单id", example = "1")
    @TableField("order_id")
    private String orderId;

    @ApiModelProperty(value = "planId", example = "75520ca6-fb4d-4af0-a06c-6c6a27da265d")
    @TableField("plan_id")
    private String planId;

    @ApiModelProperty(value = "candidateId", example = "f26b821d-ea97-4461-b336-547a72e6a845")
    @TableField("candidate_id")
    private String candidateId;
}