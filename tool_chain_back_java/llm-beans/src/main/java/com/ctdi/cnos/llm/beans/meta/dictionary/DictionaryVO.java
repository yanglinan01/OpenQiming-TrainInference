/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.dictionary;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Dictionary 字典VO
 *
 * @author huangjinhua
 * @since 2024/4/23
 */
@Data
@ApiModel("字典 vo类")
public class DictionaryVO {
    @ApiModelProperty(value = "字典类别ID", example = "1000001")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dictId;
    @ApiModelProperty(value = "字典类别编码", example = "IS_VALID")
    private String dictType;
    @ApiModelProperty(value = "字典类别名称", example = "是否有效")
    private String dictName;
    @ApiModelProperty(value = "字典类别状态（0正常 1禁用）", example = "0")
    private String dictStatus;
    @ApiModelProperty(value = "字典项ID", example = "4")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dictItemId;
    @ApiModelProperty(value = "字典项排序", example = "4")
    private Long dictItemSort;
    @ApiModelProperty(value = "字典项标签", example = "0")
    private String dictItemLabel;
    @ApiModelProperty(value = "字典项值", example = "0")
    private String dictItemValue;
    @ApiModelProperty(value = "字典项样式属性（其他样式扩展）", example = "")
    private String dictItemCssClass;
    @ApiModelProperty(value = "字典项表格回显样式", example = "")
    private String dictItemListClass;
    @ApiModelProperty(value = "字典项状态（0正常 1禁用）", example = "0")
    private String dictItemStatus;
    @ApiModelProperty(value = "字典项父节点", example = "1")
    private Long dictItemParentId;
    @ApiModelProperty(value = "字典项扩展属性1", example = "")
    private String dictItemExtField1;
    @ApiModelProperty(value = "字典项扩展属性2", example = "")
    private String dictItemExtField2;
    @ApiModelProperty(value = "字典项扩展属性3", example = "")
    private String dictItemExtField3;
}
