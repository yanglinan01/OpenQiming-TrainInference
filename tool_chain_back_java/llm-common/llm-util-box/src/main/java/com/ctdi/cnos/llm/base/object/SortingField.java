package com.ctdi.cnos.llm.base.object;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 排序字段对象。
 *
 * @author laiqi
 * @since 2024/7/12
 */
@NoArgsConstructor
@Setter
@Getter
@ApiModel("排序字段对象")
public class SortingField {

    /**
     * 排序字段。
     */
    @ApiModelProperty(value = "排序字段", example = "id")
    private String fieldName;
    /**
     * 排序方向。true为升序，否则降序。
     */
    @ApiModelProperty(value = "排序方向。true为升序，否则降序。", example = "false")
    private Boolean asc = true;

    /**
     * 构造函数。
     * @param fieldName
     * @param asc
     */
    private SortingField(String fieldName, Boolean asc) {
        this.fieldName = fieldName;
        this.asc = asc;
    }

    /**
     * 创建升序排序字段对象。
     * @param fieldName
     * @return
     */
    public static SortingField asc(String fieldName) {
        return new SortingField(fieldName, true);
    }

    /**
     * 创建降序排序字段对象。
     * @param fieldName
     * @return
     */
    public static SortingField desc(String fieldName) {
        return new SortingField(fieldName, false);
    }
}