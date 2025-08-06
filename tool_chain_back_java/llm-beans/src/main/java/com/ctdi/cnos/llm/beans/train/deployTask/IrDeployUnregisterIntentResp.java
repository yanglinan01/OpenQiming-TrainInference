package com.ctdi.cnos.llm.beans.train.deployTask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/16 10:25
 * @Description 卸载意图识别Lora模型接口出参
 */
@Data
@ApiModel("卸载意图识别Lora模型接口出参")
public class IrDeployUnregisterIntentResp {
    @ApiModelProperty("响应码")
    private int code;

    @ApiModelProperty("响应信息")
    private String message;

    @ApiModelProperty("返回信息")
    private Map data;
}
