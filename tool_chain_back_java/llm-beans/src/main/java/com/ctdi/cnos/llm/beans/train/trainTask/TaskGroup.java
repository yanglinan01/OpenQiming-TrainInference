package com.ctdi.cnos.llm.beans.train.trainTask;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/24 17:01
 * @Description 训练部署任务组表
 */
@Data
@ApiModel("训练任务数据集关系")
@TableName("train.mm_task_group")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskGroup {

    @ApiModelProperty(value = "ID，修改时必填!", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "任务组名称", example = "")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "部署状态", example = "")
    @TableField("deploy_status")
    private String deployStatus;


    @ApiModelProperty(value = "区域编码", example = "")
    @TableField("region_code")
    private String regionCode;

    @ApiModelProperty(value = "项目id", example = "")
    @TableField("project_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long projectId;

    @ApiModelProperty(value = "模型ID，meta.mm_model.id", example = "")
    @TableField("model_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modelId;

    @ApiModelProperty(value = "父任务ID，train.mm_train_task.id", example = "")
    @TableField("parent_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    @ApiModelProperty(value = "训练接口目标，,mate.mm_cluster.code", example = "")
    @TableField("train_target")
    private String trainTarget;

    @ApiModelProperty(value = "训练类型，字典MODEL_TRAIN_TYPE", example = "")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "训练分类，字典MODEL_TRAIN_CLASSIFY", example = "")
    @TableField("classify")
    private String classify;


    @ApiModelProperty(value = "创建人", example = "")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("creator_id")
    private Long creatorId;

    @ApiModelProperty(value = "创建人", example = "")
    @TableField("create_date")
    private Date createDate;

    @ApiModelProperty(value = "更新人", example = "")
    @TableField("modifier_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modifierId;

    @ApiModelProperty(value = "更新时间", example = "")
    @TableField("modify_date")
    private Date modifyDate;

}
