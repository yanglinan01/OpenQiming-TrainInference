package com.ctdi.cnos.llm.beans.train.IntentRecognition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/5 14:08
 * @Description 意图识别语料列表请求参
 */
@ApiModel(description = "意图识别语料列表请求参")
@Data
public class IntentRecognitionCorpusReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "专业筛选，根据3c信息取code，拼接:一级code,二级code", example = "jrw,zhw")
    private String professionalLabel;

    @ApiModelProperty(value = "创建人工号", example = "1234@HQ")
    private String createBy;

    @ApiModelProperty(value = "页号", example = "1")
    private Integer pageNum;

    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize;
}
