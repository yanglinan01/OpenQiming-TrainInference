package com.ctdi.cnos.llm.system.province.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 省份表 实体对象。
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@Getter
@Setter
@TableName("meta.mm_province")
@ApiModel(value = "Province对象", description = "省份表")
public class Province {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    @TableField("name")
    private String name;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    @TableField("code")
    private String code;

    /**
     * 省份缩写
     */
    @ApiModelProperty(value = "省份缩写")
    @TableField("abbreviation")
    private String abbreviation;

}