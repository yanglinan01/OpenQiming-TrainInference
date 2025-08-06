package com.ctdi.cnos.llm.beans.train.trainEval;

import com.ctdi.cnos.llm.base.object.Groups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 训练任务c-eval 评估 Dto对象。
 *
 * @author huangjinhua
 * @since 2024/09/04
 */
@ApiModel(description = "TrainTaskEvalDto对象")
@Data
public class TrainTaskEvalDTO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", required = true)
    @NotNull(message = "数据验证失败，ID不能为空！", groups = {Groups.UPDATE.class})
    private Long id;

    /**
     * 训练任务ID，train.mm_train_task.id
     */
    @ApiModelProperty(value = "训练任务ID，train.mm_train_task.id", required = true)
    @NotNull(message = "数据验证失败，训练任务ID，train.mm_train_task.id不能为空！")
    private Long trainTaskId;

    /**
     * 评估接口目标，GZ：贵州，QD青岛
     */
    @ApiModelProperty(value = "评估接口目标，GZ：贵州，QD青岛，mate.mm_cluster.code")
    private String evalTarget;

    /**
     * 理工评分
     */
    @ApiModelProperty(value = "理工评分")
    private Double stem;

    /**
     * 社会科学评分
     */
    @ApiModelProperty(value = "社会科学评分")
    private Double socialScience;

    /**
     * 人文科学评分
     */
    @ApiModelProperty(value = "人文科学评分")
    private Double humanity;

    /**
     * 其他评分
     */
    @ApiModelProperty(value = "其他评分")
    private Double other;

    /**
     * 平均评分
     */
    @ApiModelProperty(value = "平均评分")
    private Double average;

    /**
     * 评估状态，字典MODEL_C_EVAL_STATUS
     */
    @ApiModelProperty(value = "评估状态，字典MODEL_C_EVAL_STATUS")
    private String status;

    /**
     * 评估信息
     */
    @ApiModelProperty(value = "评估信息")
    private String evalInfo;
}