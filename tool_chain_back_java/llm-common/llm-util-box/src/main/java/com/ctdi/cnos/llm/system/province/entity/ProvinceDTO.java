package com.ctdi.cnos.llm.system.province.entity;

import com.ctdi.cnos.llm.base.object.Groups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 省份表 DTO对象。
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@ApiModel(description = "ProvinceDTO对象")
@Data
public class ProvinceDTO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "数据验证失败，主键不能为空！", groups = {Groups.UPDATE.class})
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "数据验证失败，名称不能为空！")
    private String name;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String code;

    /**
     * 省份缩写
     */
    @ApiModelProperty(value = "省份缩写")
    private String abbreviation;

}