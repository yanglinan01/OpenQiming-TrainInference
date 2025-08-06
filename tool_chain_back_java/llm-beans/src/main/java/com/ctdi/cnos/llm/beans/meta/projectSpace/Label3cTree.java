package com.ctdi.cnos.llm.beans.meta.projectSpace;

import com.baomidou.mybatisplus.annotation.*;
import com.ctdi.cnos.llm.base.object.BaseModel;

import java.lang.reflect.Field;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 标签树表 实体对象。
 *
 * @author 
 * @since 2025/06/10
 */
@Getter
@Setter
@TableName("meta.mm_label_3c_tree")
@ApiModel(value = "Label3cTree对象", description = "标签树表")
public class Label3cTree extends BaseModel {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联的字典类型ID
     */
	@ApiModelProperty(value = "关联的字典类型", required = true)
    @TableField("type")
    private String type;

    /**
     * 标签编码
     */
	@ApiModelProperty(value = "标签编码", required = true)
    @TableField("code")
    private String code;

    /**
     * 标签名称
     */
	@ApiModelProperty(value = "标签名称", required = true)
    @TableField("name")
    private String name;

    /**
     * 层级
     */
	@ApiModelProperty(value = "层级", required = true)
    @TableField("level")
    private String level;

    /**
     * 父级ID，-1表示顶级
     */
	@ApiModelProperty(value = "父级ID，-1表示顶级", required = true)
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 备注说明
     */
    @ApiModelProperty(value = "备注说明")
    @TableField("remark")
    private String remark;



}