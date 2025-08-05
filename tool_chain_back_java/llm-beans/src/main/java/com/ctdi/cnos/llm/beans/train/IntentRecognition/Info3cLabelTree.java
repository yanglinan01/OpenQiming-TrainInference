package com.ctdi.cnos.llm.beans.train.IntentRecognition;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/6 10:03
 * @Description 3c信息查询标签信息
 */
@ApiModel(description = "3c信息查询标签信息")
@Data
public class Info3cLabelTree {

    @ApiModelProperty(value = "id", example = "")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal id;

    @ApiModelProperty(value = "编码", example = "guihua")
    private String code;

    @ApiModelProperty(value = "名称", example = "规划")
    private String name;

    @ApiModelProperty(value = "等级", example = "1")
    private String level;

    @ApiModelProperty(value = "父级id", example = "-1")
    private Integer parentId;

    @ApiModelProperty(value = "备注", example = "非必填，未来可以开发后台AI自动识别，减少人工工作量")
    private String remark;

    @ApiModelProperty(value = "字", example = "非必填，未来可以开发后台AI自动识别，减少人工工作量")
    private List<Info3cLabelTree> children;
}
