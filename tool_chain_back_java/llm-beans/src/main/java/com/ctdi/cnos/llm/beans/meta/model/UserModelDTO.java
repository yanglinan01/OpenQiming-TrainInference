package com.ctdi.cnos.llm.beans.meta.model;

import com.ctdi.cnos.llm.base.object.Groups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * 用户模型关系表 DTO对象。
 *
 * @author wangyb
 * @since 2024/11/14
 */
@ApiModel(description = "UserModelDTO对象")
@Data
public class UserModelDTO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", required = true)
    @NotNull(message = "数据验证失败，ID不能为空！", groups = {Groups.UPDATE.class})
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    @NotNull(message = "数据验证失败，userId不能为空！", groups = {Groups.ADD.class, Groups.UPDATE.class})
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
    @NotNull(message = "数据验证失败，模型ID不能为空！")
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
    @NotBlank(message = "数据验证失败，模型权限用途不能为空！", groups = {Groups.ADD.class})
    private String usage;

    /**
     * 模型ID列表
     */
    @ApiModelProperty(value = "模型ID列表")
    private Set<Long> modelIdList;

    /**
     * 模型ID列表
     */
    @ApiModelProperty(value = "用户ID列表")
    private Set<Long> userIdList;
    /**
     * 模型ID列表
     */
    @ApiModelProperty(value = "是否按用户ID/模型ID w维度全量删除")
    private Boolean deleteBindingAll = false;

}