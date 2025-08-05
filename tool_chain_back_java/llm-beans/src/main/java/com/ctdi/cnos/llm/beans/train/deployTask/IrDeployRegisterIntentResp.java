package com.ctdi.cnos.llm.beans.train.deployTask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/16 10:10
 * @Description 动态注册意图识别Lora模型回参
 */
@Data
@ApiModel("动态注册意图识别Lora模型回参")
public class IrDeployRegisterIntentResp {

    @ApiModelProperty("响应码")
    private int code;

    @ApiModelProperty("响应信息")
    private String message;

    @ApiModelProperty("返回信息")
    private Map data;
}
