package com.ctdi.cnos.llm.beans.train.trainTask;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/24 17:07
 * @Description
 */
@ApiModel(description = "TaskGroupVO对象")
@Data
public class TaskGroupVO {

    @ApiModelProperty(value = "ID，修改时必填!", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "任务组名称", example = "")
    private String name;

    @ApiModelProperty(value = "部署状态", example = "")
    private String deployStatus;

    @ApiModelProperty(value = "区域编码", example = "")
    private String regionCode;

    @ApiModelProperty(value = "项目id", example = "")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long projectId;

    @ApiModelProperty(value = "模型ID，meta.mm_model.id", example = "")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modelId;

    @ApiModelProperty(value = "父任务ID，train.mm_train_task.id", example = "")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    @ApiModelProperty(value = "训练接口目标，,mate.mm_cluster.code", example = "")
    private String trainTarget;

    @ApiModelProperty(value = "训练类型，字典MODEL_TRAIN_TYPE", example = "")
    private String type;

    @ApiModelProperty(value = "训练分类，字典MODEL_TRAIN_CLASSIFY", example = "")
    private String classify;

    @ApiModelProperty(value = "创建人", example = "")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long creatorId;

    @ApiModelProperty(value = "创建人", example = "")
    @JsonSerialize(using = ToStringSerializer.class)
    private Date createDate;

    @ApiModelProperty(value = "更新人", example = "")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modifierId;

    @ApiModelProperty(value = "更新时间", example = "")
    private Date modifyDate;

    @ApiModelProperty(value = "组或者任务 1：任务，2：组", example = "")
    private String taskOrGroup;

    @ApiModelProperty(value = "部署任务id", example = "")
    private String deployTaskId;
}
