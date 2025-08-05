package com.ctdi.cnos.llm.beans.meta.projectSpace;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 项目空间信息表 Vo对象。
 *
 * @author 
 * @since 2025/06/05
 */
@ApiModel(description = "ProjectSpaceVO对象")
@Data
public class ProjectSpaceVO {

    /**
     * 项目ID，主键
     */
    @ApiModelProperty(value = "项目ID，主键")
	private Long projectId;
	
    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
	private String projectName;
	
    /**
     * 项目所属区域
     */
    @ApiModelProperty(value = "项目所属区域")
	private String projectRegion;

    @ApiModelProperty(value = "项目所属区域名称")
    private String projectRegionName;
	
    /**
     * 项目负责人
     */
    @ApiModelProperty(value = "项目负责人")
	private String projectLeader;
	
    /**
     * 项目分类标签
     */
    @ApiModelProperty(value = "项目分类标签")
	private String projectLabel;

    @ApiModelProperty(value = "项目分类标签名称")
    private String projectLabelName;
	
    /**
     * 项目状态
     */
    @ApiModelProperty(value = "项目状态")
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

    @ApiModelProperty(value = "项目空间管理员名称集合" )
    private String adminUserNames;

    /**
     * 用户与角色关联信息
     */
    @ApiModelProperty(value = "用户与角色关联信息")
    private List<UserAndRole> userAndRoles;

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
         * 角色ID
         */
        @ApiModelProperty(value = "角色名称")
        private String roleName;

        /**
         * 用户ID列表
         */
        @ApiModelProperty(value = "用户ID列表")
        private List<Long> userIds;
    }
	
}