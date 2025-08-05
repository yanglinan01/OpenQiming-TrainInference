package com.ctdi.cnos.llm.beans.meta.group;

import com.ctdi.cnos.llm.base.object.BaseVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户组_用户关系 Vo对象。
 *
 * @author wangyb
 * @since 2024/09/23
 */
@ApiModel(description = "GroupUserVO对象")
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GroupUserVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	
    /**
     * 用户组ID
     */
    @ApiModelProperty(value = "用户组ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long groupId;
	
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;


    private String userIdsStr;
    private List<String> userIds;
    private String groupName;
}