package com.ctdi.cnos.llm.beans.meta.group;

import com.ctdi.cnos.llm.system.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户组 Vo对象。
 *
 * @author wangyb
 * @since 2024/09/23
 */
@ApiModel(description = "GroupVO对象")
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GroupVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
	@JsonSerialize(using = ToStringSerializer.class)
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

    /**
     * 组内用户
     */
    @ApiModelProperty(value = "组内用户")
    private List<User> users;
}