package com.ctdi.cnos.llm.beans.train.IntentRecognition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/16 19:31
 * @Description 意图识别Lora模型通用接口入参
 */
@ApiModel(description = "意图识别Lora模型通用接口入参")
@Data
public class IntentRecognizeReq {

    @ApiModelProperty(value = "意图识别类型", example = "")
    private String intent_type;

    @ApiModelProperty(value = "大模型请求信息", example = "")
    private List<IntentRecognizeMessage> messages;

    @ApiModelProperty(value = "模型温度，控制随意性（默认0.3）", example = "")
    private Float temperature;

    @ApiModelProperty(value = "会话最大token长度（默认1024*3）", example = "")
    private Integer max_tokens;

    @ApiModelProperty(value = "业务参数，例如区域、省份、来源、识别结果、用户输入等", example = "")
    private Map params;
}
