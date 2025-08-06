/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.train.trainTask;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 训练任务回调
 *
 * @author huangjinhua
 * @since 2024/9/9
 */
@Data
@ApiModel("训练任务回调")
public class TrainTaskCallback {

    @ApiModelProperty(value = "ID，修改时必填!", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "任务状态，字典TRAIN_TASK_STATUS", example = "")
    private String status;

    @ApiModelProperty(value = "总迭代次数", example = "1000")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long iterateTotal;
    @ApiModelProperty(value = "当前迭代次数", example = "70")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long iterateCurr;

    @ApiModelProperty(value = "已运行时间", example = "10分30秒")
    private String runtime;
    @ApiModelProperty(value = "预计剩余训练时间", example = "1分30秒")
    private String remainTime;
    @ApiModelProperty(value = "训练结果信息", example = "Resource limit reached")
    private String result;

    @ApiModelProperty(value = "训练Loss数据(有序loss值按,分隔)", example = "100, 200, 300, 222, 333")
    private String trainingLoss;

    @ApiModelProperty(value = "Loss趋势分析", example = "模型正在有效学习，并且能够在训练集外的数据上表现良好。")
    private String lossTrend;

}