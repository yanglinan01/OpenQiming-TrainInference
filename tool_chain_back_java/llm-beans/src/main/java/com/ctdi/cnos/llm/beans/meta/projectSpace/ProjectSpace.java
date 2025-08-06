package com.ctdi.cnos.llm.beans.meta.projectSpace;

import com.baomidou.mybatisplus.annotation.*;
import com.ctdi.cnos.llm.base.object.BaseModel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 项目空间信息表 实体对象。
 *
 * @author 
 * @since 2025/06/05
 */
@Getter
@Setter
@TableName("meta.mm_project_space")
@ApiModel(value = "ProjectSpace对象", description = "项目空间信息表")
public class ProjectSpace extends BaseModel {
    /**
     * 项目ID，主键
     */
	@ApiModelProperty(value = "项目ID，主键", required = true)
    @TableId(value = "project_id", type = IdType.AUTO)
    private Long projectId;

    /**
     * 项目名称
     */
	@ApiModelProperty(value = "项目名称", required = true)
    @TableField("project_name")
    private String projectName;

    /**
     * 项目所属区域
     */
	@ApiModelProperty(value = "项目所属区域")
    @TableField("project_region")
    private String projectRegion;

    /**
     * 项目负责人
     */
	@ApiModelProperty(value = "项目负责人")
    @TableField("project_leader")
    private String projectLeader;

    /**
     * 项目分类标签
     */
    @ApiModelProperty(value = "项目分类标签")
    @TableField("project_label")
    private String projectLabel;

    /**
     * 项目状态
     */
	@ApiModelProperty(value = "项目状态用途，字典PROJECT_SPACE_STATUS 1是正常 2 是关闭", required = true )
    @TableField("status")
    private String status;




    /**
     * 项目详细描述
     */
    @ApiModelProperty(value = "项目详细描述")
    @TableField("description")
    private String description;

    /**
     * 项目状态
     */
    @ApiModelProperty(value = "项目空间类型用途，字典PROJECT_TYPE 1是公共 2 是私人", required = true )
    @TableField("project_type")
    private String projectType;


    /**
     * 智能体同步信息数据字段
     */
    @ApiModelProperty(value = "智能体同步信息数据字段")
    @TableField("agent_uuid")
    private String agentUuid;

    /**
     * 智能体同步信息数据字段
     */
    @ApiModelProperty(value = "智能体同步信息数据字段")
    @TableField("agent_name")
    private String agentName;






}