package com.ctdi.cnos.llm.beans.train.trainTask;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/10 17:06
 * @Description 训练任务数据集关系
 */
@Data
@ApiModel("训练任务数据集关系")
@TableName("train.mm_train_task")
public class TrainDatasetJoin {
    @ApiModelProperty(value = "ID，修改时必填!", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "训练任务id", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("task_id")
    private Long taskId;

    @ApiModelProperty(value = "数据集id", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("data_set_id")
    private BigDecimal dataSetId;
}
