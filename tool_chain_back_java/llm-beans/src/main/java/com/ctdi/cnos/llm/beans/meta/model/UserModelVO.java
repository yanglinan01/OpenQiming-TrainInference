package com.ctdi.cnos.llm.beans.meta.model;

import com.ctdi.cnos.llm.base.object.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户模型关系表 Vo对象。
 *
 * @author wangyb
 * @since 2024/11/14
 */
@ApiModel(description = "UserModelVO对象")
@Data
public class UserModelVO extends BaseModel {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * 人力工号
     */
    @ApiModelProperty(value = "人力工号")
    private String employeeNumber;

    /**
     * 模型ID
     */
    @ApiModelProperty(value = "模型ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modelId;

    /**
     * 模型名称
     */
    @ApiModelProperty(value = "模型名称")
    private String modelName;

    /**
     * 模型权限用途，字典MODEL_AUTH_USAGE
     */
    @ApiModelProperty(value = "模型权限用途，字典MODEL_AUTH_USAGE")
    private String usage;


}