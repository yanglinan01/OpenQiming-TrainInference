package com.ctdi.cnos.llm.beans.meta.evaluation;

import com.ctdi.cnos.llm.base.object.BaseVO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 问答对测试数据集评估 Vo对象。
 *
 * @author laiqi
 * @since 2024/09/03
 */
@ApiModel(description = "PrTestSetEvaluationVo对象")
@Data
public class PrTestSetEvaluationVO extends BaseVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 数据集ID
     */
    @ApiModelProperty(value = "数据集ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dataSetId;

    /**
     * 模型任务ID
     */
    @ApiModelProperty(value = "模型任务ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modelTaskId;

    /**
     * 模型任务名称
     */
    @ApiModelProperty(value = "模型任务名称")
    private String modelTaskName;

    /**
     * 数据集名称
     */
    @ApiModelProperty(value = "数据集名称")
    private String dataSetName;

    /**
     * 评估状态
     */
    @ApiModelProperty(value = "评估状态")
    private String status;


    /**
     * 部署任务状态
     */
    @ApiModelProperty(value = "部署任务状态")
    private String deployStatus;

    /**
     * 部署任务目标位置
     */
    @ApiModelProperty(value = "部署任务目标位置")
    private String deployTarget;

    /**
     * 部署任务推理url
     */
    @ApiModelProperty(value = "部署任务推理url")
    private String deployUrl;


    /**
     * 评估类型(0强化; 1普通)
     */
    @ApiModelProperty(value = "评估类型(0强化; 1普通)")
    private String type;

    /**
     * 部署完成时间
     */
    @ApiModelProperty(value = "")
    private Date deployFinishDate;

    /**
     * 评估状态字典
     */
    @ApiModelProperty(value = "评估状态字典")
    private Map<String, String> statusDictMap;

    /**
     * 问答详情ID
     */
    @ApiModelProperty(value = "问答对详情ID")
    private Long promptResponseDetailId;

    /**
     * prompt问题序号
     */
    @ApiModelProperty(value = "prompt问题序号")
    private Long questionId;

    /**
     * prompt问
     */
    @ApiModelProperty(value = "prompt问")
    private String prompt;

    /**
     * prompt答
     */
    @ApiModelProperty(value = "prompt答")
    private String response;

    /**
     * 是否构建强化学习
     */
    @ApiModelProperty(value = "是否构建强化学习")
    private Boolean built;

    /**
     * 评估类型字典
     */
    @ApiModelProperty(value = "评估类型字典")
    private Map<String, String> typeDictMap;

    /**
     * 温度
     */
    @ApiModelProperty(value = "温度")
    private Float temperature;

    /**
     * max_tokens
     */
    @ApiModelProperty(value = "max_tokens")
    private Integer maxTokens;
}