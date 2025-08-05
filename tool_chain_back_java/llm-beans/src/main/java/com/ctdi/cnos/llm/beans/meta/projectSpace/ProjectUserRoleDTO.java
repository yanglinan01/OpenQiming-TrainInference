package com.ctdi.cnos.llm.beans.meta.projectSpace;

import com.ctdi.cnos.llm.base.object.Groups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 项目空间用户角色关联信息表 DTO对象。
 *
 * @author 
 * @since 2025/06/05
 */
@ApiModel(description = "ProjectUserRoleDTO对象")
@Data
public class ProjectUserRoleDTO {

    /**
     * 项目ID
     */
	@ApiModelProperty(value = "项目ID", required = true)
	@NotNull(message = "数据验证失败，项目ID不能为空！")
	private Long projectId;
	
    /**
     * 用户ID
     */
	@ApiModelProperty(value = "用户ID", required = true)
	@NotNull(message = "数据验证失败，用户ID不能为空！")
	private Long userId;
	
    /**
     * 角色ID
     */
	@ApiModelProperty(value = "角色ID", required = true)
	@NotNull(message = "数据验证失败，角色ID不能为空！")
	private Long roleId;
	
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
	
}