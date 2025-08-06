package com.ctdi.cnos.llm.beans.train.trainEval;

import com.ctdi.cnos.llm.base.object.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 训练任务c-eval 评估 回调对象对象。
 *
 * @author huangjinhua
 * @since 2024/09/04
 */
@Getter
@Setter
@ApiModel(value = "TrainTaskEval 回调对象")
public class TrainTaskEvalCallback{

    @ApiModelProperty(value = "评估Eval任务ID", required = true)
    private Long taskId;

    @ApiModelProperty(value = "理工评分")
    private Double stem;

    @ApiModelProperty(value = "社会科学评分")
    private Double socialScience;

    @ApiModelProperty(value = "人文科学评分")
    private Double humanity;

    @ApiModelProperty(value = "其他评分")
    private Double other;

    @ApiModelProperty(value = "平均评分")
    private Double average;

    @ApiModelProperty(value = "评估状态")
    private String status;

    @ApiModelProperty(value = "评估信息")
    private String info;
}