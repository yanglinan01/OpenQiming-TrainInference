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
 * 字典类型 实体
 *
 * @author huangjinhua
 * @since 2024/4/23
 */
@Data
@ApiModel("字典类型 实体类")
public class DictType implements Serializable {
    private static final long serialVersionUID = -12685702896326562L;
    @ApiModelProperty(value = "字典主键", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dictId;
    @ApiModelProperty(value = "字典名称", example = "prompt类别")
    private String dictName;
    @ApiModelProperty(value = "字典类型", example = "PROMPT_TYPE")
    private String dictType;
    @ApiModelProperty(value = "状态（0正常 1禁用）", example = "0")
    private String status;
    @ApiModelProperty(value = "父节点", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;
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
}