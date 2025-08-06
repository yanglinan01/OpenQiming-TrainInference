package com.ctdi.cnos.llm.beans.train.IntentRecognition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/6 9:39
 * @Description
 */
@ApiModel(description = "3c信息查询类型")
@Data
public class Info3cTreeParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型", example = "C3")
    private String type;

    @ApiModelProperty(value = "类型名", example = "流程领域")
    private String typeName;

    @ApiModelProperty(value = "标签树", example = "")
    private List<Info3cLabelTree> labelTrees;
}
