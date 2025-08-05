package com.ctdi.cnos.llm.beans.train.deployTask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/16 10:22
 * @Description 卸载意图识别Lora模型接口入参
 */
@Data
@ApiModel("卸载意图识别Lora模型接口入参")
public class IrDeployUnregisterIntentReq {

    @ApiModelProperty("已注册的意图识别类型用于标识意图模型")
    private String intent_type;
}
