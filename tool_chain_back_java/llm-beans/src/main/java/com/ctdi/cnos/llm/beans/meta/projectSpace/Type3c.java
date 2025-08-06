package com.ctdi.cnos.llm.beans.meta.projectSpace;

import com.baomidou.mybatisplus.annotation.*;
import com.ctdi.cnos.llm.base.object.BaseModel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典类型表 实体对象。
 *
 * @author 
 * @since 2025/06/10
 */
@Getter
@Setter
@TableName("meta.mm_type_3c")
@ApiModel(value = "Type3c对象", description = "字典类型表")
public class Type3c extends BaseModel {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类型编码
     */
	@ApiModelProperty(value = "类型编码", required = true)
    @TableField("type")
    private String type;

    /**
     * 类型名称
     */
	@ApiModelProperty(value = "类型名称", required = true)
    @TableField("type_name")
    private String typeName;


}