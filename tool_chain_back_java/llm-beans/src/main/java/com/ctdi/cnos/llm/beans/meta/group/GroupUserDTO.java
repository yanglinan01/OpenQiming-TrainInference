package com.ctdi.cnos.llm.beans.meta.group;

import com.ctdi.cnos.llm.base.object.Groups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户组_用户关系 DTO对象。
 *
 * @author wangyb
 * @since 2024/09/23
 */
@ApiModel(description = "GroupUserDTO对象")
@Data
public class GroupUserDTO {

    /**
     * 主键
     */
	@ApiModelProperty(value = "主键", required = true)
	@NotNull(message = "数据验证失败，主键不能为空！", groups = {Groups.UPDATE.class})
	private Long id;
	
    /**
     * 用户组ID
     */
    @ApiModelProperty(value = "用户组ID")
	private Long groupId;
	
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
	private Long userId;
	
}