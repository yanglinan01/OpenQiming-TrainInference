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
 * 用户组 实体对象。
 *
 * @author wangyb
 * @since 2024/09/23
 */
@Getter
@Setter
@TableName("meta.mm_group")
@ApiModel(value = "Group对象", description = "用户组")
public class Group extends BaseModel {
    /**
     * 主键
     */
	@ApiModelProperty(value = "主键", required = true)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 组名
     */
    @ApiModelProperty(value = "组名")
    @TableField("name")
    private String name;

    /**
     * 是否合理(0是; 1否)
     */
    @ApiModelProperty(value = "是否合理(0是; 1否)")
    @TableField("is_valid")
    private Integer isValid;

}