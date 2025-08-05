/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.train.trainTask;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 训练任务查询参数
 *
 * @author huangjinhua
 * @since 2024/10/24
 */
@Data
@ApiModel("训练任务查询参数")
@Accessors(chain = true)
public class TrainTaskQuery {

    @ApiModelProperty(value = "页大小，默认为20", example = "20")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pageSize = 20L;
    @ApiModelProperty(value = "当前页，默认为1", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long currentPage = 1L;
    @ApiModelProperty(value = "名称", required = true, example = "训练作业")
    private String name;
    @ApiModelProperty(value = "模型ID，meta.mm_model.id", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modelId;

    @ApiModelProperty(value = "模型名称", example = "xxxx")
    private String modelName;
    @ApiModelProperty(value = "父任务ID，train.mm_train_task.id", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;
    @ApiModelProperty(value = "父任务名称", example = "xxx")
    private String parentName;

    @ApiModelProperty(value = "训练接口目标，GZ：贵州，QD青岛，mate.mm_cluster.code", example = "GZ")
    private String trainTarget;

    @ApiModelProperty(value = "训练分类，字典MODEL_TRAIN_CLASSIFY，查询时可逗号分割", example = "learn")
    private String classify;

    @ApiModelProperty(value = "训练类型，字典MODEL_TRAIN_TYP，查询时可逗号分割", example = "LORA")
    private String type;

    @ApiModelProperty(value = "任务状态，字典TRAIN_TASK_STATUS", example = "")
    private String status;
    @ApiModelProperty(value = "是否是查看资源占用情况，0是，1否 用于资源管理使用资源", example = "1")
    private String resourceOccupy;

    @ApiModelProperty(value = "部署任务归属 1：工具链 2：项目空间", example = "1")
    private String deployBelong;

    @ApiModelProperty(value = "项目id", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long projectId;

    @ApiModelProperty(value = "组或者任务 task：任务，group：组", example = "")
    private String taskOrGroup;

}