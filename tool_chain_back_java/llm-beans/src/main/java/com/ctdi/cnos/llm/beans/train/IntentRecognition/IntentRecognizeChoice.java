package com.ctdi.cnos.llm.beans.train.IntentRecognition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/16 19:37
 * @Description 结果列表
 */
@ApiModel(description = "结果列表")
@Data
public class IntentRecognizeChoice {

    @ApiModelProperty(value = "响应码", example = "")
    private List<String> message;
}
