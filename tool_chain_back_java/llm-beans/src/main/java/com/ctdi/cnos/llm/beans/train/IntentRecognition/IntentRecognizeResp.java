package com.ctdi.cnos.llm.beans.train.IntentRecognition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/16 19:34
 * @Description 意图识别Lora模型通用接口回参
 */
@ApiModel(description = "意图识别Lora模型通用接口回参")
@Data
public class IntentRecognizeResp {

    @ApiModelProperty(value = "响应码", example = "")
    private int code;

    @ApiModelProperty(value = "响应信息", example = "")
    private String message;

    @ApiModelProperty(value = "返回结果", example = "")
    private Map data;

    @ApiModelProperty(value = "生成结果列表，每个元素是一个包含回复信息的对象", example = "")
    private List<IntentRecognizeChoice> choices;
}
