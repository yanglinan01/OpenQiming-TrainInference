package com.ctdi.cnos.llm.beans.train.IntentRecognition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/16 19:38
 * @Description 回复消息对象
 */
@ApiModel(description = "回复消息对象")
@Data
public class IntentRecognizeMessage {

    @ApiModelProperty(value = "回复消息的角色，通常为 assistant", example = "")
    private String role;

    @ApiModelProperty(value = "回复消息的内容", example = "")
    private String content;
}
