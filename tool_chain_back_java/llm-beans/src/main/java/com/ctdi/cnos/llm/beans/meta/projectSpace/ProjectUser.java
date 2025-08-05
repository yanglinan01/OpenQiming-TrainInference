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
 * 项目空间用户关联信息表 实体对象。
 *
 * @author 
 * @since 2025/06/05
 */
@Getter
@Setter
@TableName("meta.mm_project_user")
@ApiModel(value = "ProjectUser对象", description = "项目空间用户关联信息表")
public class ProjectUser extends BaseModel {
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    @TableField("project_id")
    private Long projectId;

    /**
     * 用户ID
     */
	@ApiModelProperty(value = "用户ID", required = true)
    @TableField("user_id")
    private Long userId;

    /**
     * 状态
     */
	@ApiModelProperty(value = "状态", required = true)
    @TableField("status")
    private String status;


    /**
     * 项目详细描述
     */
    @ApiModelProperty(value = "项目详细描述")
    @TableField("description")
    private String description;

}