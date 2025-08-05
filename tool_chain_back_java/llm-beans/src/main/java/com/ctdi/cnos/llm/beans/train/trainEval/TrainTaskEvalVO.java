package com.ctdi.cnos.llm.beans.train.trainEval;

import com.ctdi.cnos.llm.base.object.BaseVO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 训练任务c-eval 评估 Vo对象。
 *
 * @author huangjinhua
 * @since 2024/09/04
 */
@ApiModel(description = "TrainTaskEvalVo对象")
@Data
public class TrainTaskEvalVO extends BaseVO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 训练任务ID，train.mm_train_task.id
     */
    @ApiModelProperty(value = "训练任务ID，train.mm_train_task.id")
    @JsonSerialize(using = ToStringSerializer.class)
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
    @JsonSerialize(using = ToStringSerializer.class)
    private Double stem;

    /**
     * 社会科学评分
     */
    @ApiModelProperty(value = "社会科学评分")
    @JsonSerialize(using = ToStringSerializer.class)
    private Double socialScience;

    /**
     * 人文科学评分
     */
    @ApiModelProperty(value = "人文科学评分")
    @JsonSerialize(using = ToStringSerializer.class)
    private Double humanity;

    /**
     * 其他评分
     */
    @ApiModelProperty(value = "其他评分")
    @JsonSerialize(using = ToStringSerializer.class)
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

    /**
     * 评估状态，字典MODEL_C_EVAL_STATUS
     */
    @ApiModelProperty(value = "评估状态名称")
    private String statusName;

}