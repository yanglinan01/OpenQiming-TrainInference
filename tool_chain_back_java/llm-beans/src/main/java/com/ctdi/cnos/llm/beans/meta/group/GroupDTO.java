package com.ctdi.cnos.llm.beans.meta.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户组 DTO对象。
 *
 * @author wangyb
 * @since 2024/09/23
 */
@ApiModel(description = "GroupDTO对象")
@Data
public class GroupDTO {

    /**
     * 主键
     */
	@ApiModelProperty(value = "主键", required = true)
	@NotNull(message = "数据验证失败，主键不能为空！")
	private Long id;
	
    /**
     * 组名
     */
    @ApiModelProperty(value = "组名")
	private String name;
	
    /**
     * 是否合理(0是; 1否)
     */
    @ApiModelProperty(value = "是否合理(0是; 1否)")
	private Integer isValid;
	
}