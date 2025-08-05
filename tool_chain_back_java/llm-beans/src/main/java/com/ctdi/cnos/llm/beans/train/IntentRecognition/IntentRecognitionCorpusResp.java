package com.ctdi.cnos.llm.beans.train.IntentRecognition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/5 14:18
 * @Description 意图识别语料列表请求参
 */
@ApiModel(description = "意图识别语料列表响应参")
@Data
public class IntentRecognitionCorpusResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "总条数", example = "15")
    private Integer total;

    @ApiModelProperty(value = "意图识别语料属性", example = "")
    private List<IntentRecognitionCorpusParam> rows;

    @ApiModelProperty(value = "响应码", example = "200")
    private Integer code;

    @ApiModelProperty(value = "响应消息", example = "查询成功")
    private String msg;
}
