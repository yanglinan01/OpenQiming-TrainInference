package com.ctdi.cnos.llm.base.object;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * Excel数据对象。
 *
 * @author laiqi
 * @since 2024/9/5
 */
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@ApiModel(value = "Excel数据对象")
@Data
public class ExcelDataModel implements Serializable {
    /**
     * 文件名
     */
    @ApiModelProperty("文件名")
    private final String filename;

    /**
     * 表头别名(需要有序)
     * @see java.util.LinkedHashMap
     */
    @ApiModelProperty("表头别名(需要有序)")
    private final Map<String, String> headerAlias;

    /**
     * 行数据
     */
    @ApiModelProperty("行数据")
    private final Iterable<?> rows;
}