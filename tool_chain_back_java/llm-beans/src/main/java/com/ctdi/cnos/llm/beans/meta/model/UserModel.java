package com.ctdi.cnos.llm.beans.meta.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctdi.cnos.llm.base.object.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户模型关系表 实体对象。
 *
 * @author wangyb
 * @since 2024/11/14
 */

@TableName("meta.mm_user_model")
@ApiModel(value = "UserModel对象", description = "用户模型关系表")
@Data
@Accessors(chain = true)
public class UserModel extends BaseModel {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID", required = true)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private Long userId;

    /**
     * 模型ID
     */
    @ApiModelProperty(value = "模型ID")
    @TableField("model_id")
    private Long modelId;

    /**
     * 模型权限用途，字典MODEL_AUTH_USAGE
     */
    @ApiModelProperty(value = "模型权限用途，字典MODEL_AUTH_USAGE")
    @TableField("usage")
    private String usage;

}