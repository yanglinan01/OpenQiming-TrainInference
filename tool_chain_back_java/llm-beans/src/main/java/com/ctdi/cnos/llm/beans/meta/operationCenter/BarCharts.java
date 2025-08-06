package com.ctdi.cnos.llm.beans.meta.operationCenter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 柱状图对象
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@ApiModel(description = "柱状图对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarCharts {

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String province;
    /**
     * 省份缩写
     */
    @ApiModelProperty(value = "省份缩写")
    private String abbreviation;

    /**
     * 统计量
     */
    @ApiModelProperty(value = "统计量")
    private Integer count;
}