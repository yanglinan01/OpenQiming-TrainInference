/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.train.trainTaskDemo;

import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.base.object.BaseVO;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTaskVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 训练任务demo
 *
 * @author huangjinhua
 * @since 2024/9/20
 */
@Data
@ApiModel("训练任务VO")
@Accessors(chain = true)
public class TrainTaskVO extends BaseVO {

    @ApiModelProperty(value = "ID，修改时必填!", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @ApiModelProperty(value = "名称", required = true, example = "训练作业")
    private String name;
    @ApiModelProperty(value = "模型ID，meta.mm_model.id", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modelId;
    @ApiModelProperty(value = "训练方法，字典TRAIN_TASK_METHOD", example = "1")
    private String method;
    @ApiModelProperty(value = "训练类型，字典MODEL_TRAIN_TYPE", example = "1")
    private String type;
    @ApiModelProperty(value = "数据集ID,meta.mm_data_set.id", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dataSetId;
    @ApiModelProperty(value = "超参配置", example = "")
    private List<TrainTaskParamVO> param;
    @ApiModelProperty(value = "超参配置，用于数据库存储", example = "")
    private String paramStr;

    @ApiModelProperty(value = "任务状态，字典TRAIN_TASK_STATUS", example = "")
    private String status;

    @ApiModelProperty(value = "训练接口目标，GZ：贵州，QD青岛，mate.mm_cluster.code", example = "GZ")
    private String trainTarget;

    @ApiModelProperty(value = "是否已提交到k8s队列，0是，1否 字典YES_OR_NO", example = "1")
    private String submitStatus;

    @ApiModelProperty(value = "省份名称", example = "集团")
    private String regionName;

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

    @ApiModelProperty(value = "区域编码", example = "-1")
    private String regionCode;

    @ApiModelProperty(value = "创建人名称", example = "xxxx")
    private String creatorName;
    @ApiModelProperty(value = "模型名称", example = "xxxx")
    private String modelName;
    @ApiModelProperty(value = "数据集名称", example = "xxxx")
    private String dataSetName;
    @ApiModelProperty(value = "训练状态名称", example = "训练中")
    private String statusName;
    @ApiModelProperty(value = "训练方法名称", example = "全参")
    private String methodName;
    @ApiModelProperty(value = "前面任务数", example = "3")
    private Integer waitCount;
    @ApiModelProperty(value = "统计前面任务数的任务状态,用于列表中的数量统计条件", example = "3")
    private String selectStatus;
    @ApiModelProperty(value = "某个个状态任务数量,用于首页中的任务数量统计", example = "3")
    private Long statusCount;

    @ApiModelProperty(value = "训练Loss数据(有序loss值按,分隔)", example = "100, 200, 300, 222, 333")
    private String trainingLoss;

    @ApiModelProperty(value = "训练Loss数据，按照trainingLoss值进行组装", example = "{xAxisData:[1,2,3,4,5], seriesData:[11,21,31,41,25]}")
    private JSONObject trainingLossData;

    @ApiModelProperty(value = "Loss趋势分析", example = "模型正在有效学习，并且能够在训练集外的数据上表现良好。")
    private String lossTrend;

    @ApiModelProperty(value = "集群ID，数据来源 算网调度AI-智算平台接口，id字段", example = "2341")
    private String aiClusterId;

    @ApiModelProperty(value = "集群名称，数据来源 算网调度AI-智算平台接口,title字段", example = "2341")
    private String aiClusterName;

    @ApiModelProperty(value = "权限sql")
    @JsonIgnore
    private String dataScopeSql;

    @ApiModelProperty(value = "部署任务列表", example = "")
    private List<DeployTaskVO> deployTaskList;

}