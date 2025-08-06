package com.ctdi.cnos.llm.beans.meta.projectSpace;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 项目空间用户角色关联信息表 Vo对象。
 *
 * @author 
 * @since 2025/06/05
 */
@ApiModel(description = "ProjectUserRoleVO对象")
@Data
public class ProjectUserRoleVO {

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long projectId;
	
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;

    @ApiModelProperty(value = "用户真实姓名")
    private String name;

    @ApiModelProperty(value = "人力编号")
    private String employeeNumber;
	
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long roleId;

    private String roleName;

	private String status;
	
    /**
     * 记录创建人
     */
    @ApiModelProperty(value = "记录创建人")
	private Long creatorId;
	
    /**
     * 记录创建时间
     */
    @ApiModelProperty(value = "记录创建时间")
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