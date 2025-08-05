package com.ctdi.cnos.llm.beans.train.deployTask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/16 10:02
 * @Description 动态注册意图识别Lora模型入参
 */
@Data
@ApiModel("动态注册意图识别Lora模型入参")
public class IrDeployRegisterIntentReq {

    @ApiModelProperty("意图识别类型用于标识意图模型（如：home_broadband, vpn_activation, digital_assistant_support）")
    private String intent_type;

    @ApiModelProperty("Lora模型在nfs中的路径")
    private String model;

    @ApiModelProperty("提示词内容，或者提示词文件在nfs路径，与下面参数配合使用")
    private String prompt;

    @ApiModelProperty("是否是提示词文件，默认是false，如果为true，则prompt需要传递文件路径")
    private boolean use_prompt_file;
}
