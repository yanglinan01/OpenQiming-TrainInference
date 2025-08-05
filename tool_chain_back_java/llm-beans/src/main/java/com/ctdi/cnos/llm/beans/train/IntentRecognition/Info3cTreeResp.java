package com.ctdi.cnos.llm.beans.train.IntentRecognition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/6 9:14
 * @Description 3c信息查询响应
 */
@ApiModel(description = "3c信息查询")
@Data
public class Info3cTreeResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "响应码", example = "200")
    private Integer code;

    @ApiModelProperty(value = "响应消息", example = "查询成功")
    private String msg;

    @ApiModelProperty(value = "响应消息", example = "查询成功")
    private List<Info3cTreeParam> data;
}
