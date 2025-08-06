package com.ctdi.cnos.llm.beans.meta.projectSpace;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ctdi.cnos.llm.base.object.Groups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 项目空间信息表 DTO对象。
 *
 * @author 
 * @since 2025/06/05
 */
@ApiModel(description = "ProjectSpaceDTO对象")
@Data
public class ProjectSpaceDTO {

    /**
     * 项目ID，主键
     */
	@ApiModelProperty(value = "项目ID，主键", required = true)
	private Long projectId;
	
    /**
     * 项目名称
     */
	@ApiModelProperty(value = "项目名称", required = true)
	@NotBlank(message = "数据验证失败，项目名称不能为空！")			
	private String projectName;
	
    /**
     * 项目所属区域
     */
	@ApiModelProperty(value = "项目所属区域")
	@NotBlank(message = "数据验证失败，项目所属区域不能为空！")			
	private String projectRegion;
	
    /**
     * 项目负责人
     */
	@ApiModelProperty(value = "项目负责人")
	@NotBlank(message = "数据验证失败，项目负责人不能为空！")			
	private String projectLeader;
	
    /**
     * 项目分类标签
     */
    @ApiModelProperty(value = "项目分类标签")
	private String projectLabel;
	
    /**
     * 项目状态
     */
	@ApiModelProperty(value = "项目状态", required = true)
	@NotBlank(message = "数据验证失败，项目状态不能为空！")			
	private String status;
	
    /**
     * 记录创建人
     */
	@ApiModelProperty(value = "记录创建人", required = true)
	@NotBlank(message = "数据验证失败，记录创建人不能为空！")			
	private Long creatorId;
	
    /**
     * 记录创建时间
     */
	@ApiModelProperty(value = "记录创建时间", required = true)
	private Date createDate;
	
    /**
     * 最后修改人
     */
    @ApiModelProperty(value = "最后修改人")
	private Long modifierId;
	
    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间")
	private Date modifyDate;
	
    /**
     * 项目详细描述
     */
    @ApiModelProperty(value = "项目详细描述")
	private String description;

	/**
	 * 项目状态
	 */
	@ApiModelProperty(value = "项目空间类型用途，字典PROJECT_TYPE 1是公共 2 是私人", required = true )
	private String projectType;

	/**
	 * 用户与角色关联信息
	 */
	@ApiModelProperty(value = "用户与角色关联信息")
	private List<ProjectSpaceVO.UserAndRole> userAndRoles;

	/**
	 * 用户与角色关联对象
	 */
	@Data
	public static class UserAndRole {
		/**
		 * 角色ID
		 */
		@ApiModelProperty(value = "角色ID")
		private Long roleId;

		/**
		 * 用户ID列表
		 */
		@ApiModelProperty(value = "用户ID列表")
		private List<Long> userIds;
	}
}