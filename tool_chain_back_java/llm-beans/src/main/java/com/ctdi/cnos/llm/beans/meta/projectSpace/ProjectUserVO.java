package com.ctdi.cnos.llm.beans.meta.projectSpace;

import com.ctdi.cnos.llm.base.object.BaseVO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 项目空间用户关联信息表 Vo对象。
 *
 * @author 
 * @since 2025/06/05
 */
@ApiModel(description = "ProjectUserVO对象")
@Data
public class ProjectUserVO {

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
	
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
	private String status;
	
    /**
     * 记录创建人
     */
    @ApiModelProperty(value = "记录创建人")
	private String creatorId;
	
    /**
     * 记录创建时间
     */
    @ApiModelProperty(value = "记录创建时间")
	private Date createDate;
	
    /**
     * 最后修改人
     */
    @ApiModelProperty(value = "最后修改人")
	private String modifierId;
	
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
	
}