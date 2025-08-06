/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.dictionary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典值实体
 *
 * @author huangjinhua
 * @since 2024/4/23
 */
@Data
@ApiModel("字典值 实体类")
public class DictData implements Serializable {
    private static final long serialVersionUID = -12685702896326562L;
    @ApiModelProperty(value = "字典主键", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dictCode;
    @ApiModelProperty(value = "字典排序", example = "1")
    private Integer dictSort;
    @ApiModelProperty(value = "字典标签", example = "是")
    private String dictLabel;
    @ApiModelProperty(value = "字典键值", example = "0")
    private String dictValue;
    @ApiModelProperty(value = "字典类型", example = "PROMPT_TYPE")
    private String dictType;
    @ApiModelProperty(value = "样式属性（其他样式扩展）", example = "")
    private String cssClass;
    @ApiModelProperty(value = "表格回显样式", example = "")
    private String listClass;
    @ApiModelProperty(value = "状态（0正常 1禁用）", example = "0")
    private String status;
    @ApiModelProperty(value = "是否默认（Y是 N否）", example = "N")
    private String isDefault;
    @ApiModelProperty(value = "父节点", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;
    @ApiModelProperty(value = "扩展属性1", example = "")
    private String extField1;
    @ApiModelProperty(value = "扩展属性2", example = "")
    private String extField2;
    @ApiModelProperty(value = "扩展属性3", example = "")
    private String extField3;
    @ApiModelProperty(value = "创建人", example = "-1", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long creatorId;
    @ApiModelProperty(value = "创建时间", example = "2024-04-01 08:00:00", hidden = true)
    private Date createDate;
    @ApiModelProperty(value = "更新人", example = "-1", hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modifierId;
    @ApiModelProperty(value = "更新时间", example = "2024-04-01 08:10:00", hidden = true)
    private Date modifyDate;
    @ApiModelProperty(value = "备注", example = "")
    private String remark;

}