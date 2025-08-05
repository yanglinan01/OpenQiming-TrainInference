package com.ctdi.cnos.llm.beans.meta.projectSpace;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <b>请输入名称</b>
 * <p>
 * 描述<br/>
 * 作用：；<br/>
 * 限制：；<br/>
 * </p>
 *
 * @author wan.liang(79274)
 * @date 2025/6/12 09:45
 */
@ApiModel(description = "ProjectRoleUsersDTO对象")
@Data
public class ProjectRoleUsersDTO {

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    /**
     * 用户ID列表
     */
    @ApiModelProperty(value = "用户ID列表")
    private List<Long> userIds;
}
