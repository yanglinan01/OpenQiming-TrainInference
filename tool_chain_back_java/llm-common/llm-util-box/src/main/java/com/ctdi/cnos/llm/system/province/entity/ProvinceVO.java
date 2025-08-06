package com.ctdi.cnos.llm.system.province.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 省份表 Vo对象。
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@ApiModel(description = "ProvinceVO对象")
@Data
public class ProvinceVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
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


    /**
     * 省份缩写列表，用于查询
     */
    @ApiModelProperty(value = "省份缩写列表，用于查询")
    private List<String> abbreviationList;

    /**
     * 编码列表，用于查询
     */
    @ApiModelProperty(value = "编码列表，用于查询")
    private List<String> codeList;


}