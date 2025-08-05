package com.ctdi.cnos.llm.beans.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * 用于网络大模型在各个省份下各个场景赞踩情况的统计。省份、场景（助手）、总反馈数、点赞数、点踩数及点赞百分比。
 *
 * @author laiqi
 * @since 2024/9/26
 */
@FieldNameConstants
@Getter
@Setter
@ApiModel(value = "网络大模型赞踩统计结果对象", description = "网络大模型赞踩统计结果对象")
public class FeedbackSummaryVO implements Serializable {



    /**
     * 省份编码
     */
    @JsonIgnore
    @ApiModelProperty(value = "省份编码")
    private String province;

    /**
     * 场景
     */
    @JsonIgnore
    @ApiModelProperty(value = "场景")
    private String scene;

    /**
     * 总反馈数
     */
    @ApiModelProperty(value = "总反馈数")
    private Long sum;

    /**
     * 点赞数
     */
    @ApiModelProperty(value = "点赞数")
    private Long totalLikes;

    /**
     * 点踩数
     */
    @ApiModelProperty(value = "点踩数")
    private Long totalDislikes;

    /**
     * 点赞百分比
     */
    @ApiModelProperty(value = "点赞百分比")
    private Double likePercentage;

    public FeedbackSummaryVO() {
        this.sum = 0L;
        this.totalLikes = 0L;
        this.totalDislikes = 0L;
    }
}