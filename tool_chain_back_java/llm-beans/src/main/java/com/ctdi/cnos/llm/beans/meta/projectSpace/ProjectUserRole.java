package com.ctdi.cnos.llm.beans.meta.projectSpace;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctdi.cnos.llm.base.object.BaseModel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 项目空间用户角色关联信息表 实体对象。
 *
 * @author 
 * @since 2025/06/05
 */
@Getter
@Setter
@TableName("meta.mm_project_user_role")
@ApiModel(value = "ProjectUserRole对象", description = "项目空间用户角色关联信息表")
public class ProjectUserRole extends BaseModel {
    /**
     * 项目ID
     */
	@ApiModelProperty(value = "项目ID", required = true)
    @TableField("project_id")
    private Long projectId;

    /**
     * 用户ID
     */
	@ApiModelProperty(value = "用户ID", required = true)
    @TableField("user_id")
    private Long userId;

    /**
     * 角色ID
     */
	@ApiModelProperty(value = "角色ID", required = true)
    @TableField("role_id")
    private Long roleId;

    @TableField("status")
    private String status;



}