package com.ctdi.cnos.llm.beans.train.trainEval;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctdi.cnos.llm.base.object.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 训练任务c-eval 评估 实体对象。
 *
 * @author huangjinhua
 * @since 2024/09/04
 */
@Getter
@Setter
@TableName("train.mm_train_task_eval")
@ApiModel(value = "TrainTaskEval对象", description = "训练任务c-eval 评估")
public class TrainTaskEval extends BaseModel {

    @ApiModelProperty(value = "ID", required = true)
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "训练任务ID，train.mm_train_task.id", required = true)
    @TableField("train_task_id")
    private Long trainTaskId;

    @ApiModelProperty(value = "评估接口目标，GZ：贵州，QD青岛，mate.mm_cluster.code")
    @TableField("eval_target")
    private String evalTarget;

    @ApiModelProperty(value = "理工评分")
    @TableField("stem")
    private Double stem;

    @ApiModelProperty(value = "社会科学评分")
    @TableField("social_science")
    private Double socialScience;

    @ApiModelProperty(value = "人文科学评分")
    @TableField("humanity")
    private Double humanity;

    @ApiModelProperty(value = "其他评分")
    @TableField("other")
    private Double other;

    @ApiModelProperty(value = "平均评分")
    @TableField("average")
    private Double average;

    @ApiModelProperty(value = "评估状态，字典MODEL_C_EVAL_STATUS")
    @TableField("status")
    private String status;
    @ApiModelProperty(value = "评估信息")
    @TableField("eval_info")
    private String evalInfo;
}