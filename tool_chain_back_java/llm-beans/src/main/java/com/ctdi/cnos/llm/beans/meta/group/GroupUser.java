package com.ctdi.cnos.llm.beans.meta.group;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctdi.cnos.llm.base.object.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户组_用户关系 实体对象。
 *
 * @author wangyb
 * @since 2024/09/23
 */
@Getter
@Setter
@TableName("meta.mm_group_user")
@ApiModel(value = "GroupUser对象", description = "用户组_用户关系")
public class GroupUser extends BaseModel {
    /**
     * 主键
     */
	@ApiModelProperty(value = "主键", required = true)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户组ID
     */
    @ApiModelProperty(value = "用户组ID")
    @TableField("group_id")
    private Long groupId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

}