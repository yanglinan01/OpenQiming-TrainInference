package com.ctdi.cnos.llm.base.object;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.util.ModelUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * 分页数据的应答返回对象。
 *
 * @author laiqi
 * @since 2024/7/3
 */
@ApiModel("分页数据的应答返回对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    /**
     * 数据列表。
     */
    @ApiModelProperty(value = "数据列表。")
    private List<T> rows;
    /**
     * 数据总数量。
     */
    @ApiModelProperty(value = "数据总数量。")
    private Long total;

    /**
     * 为了保持前端的数据格式兼容性，在没有数据的时候，需要返回空分页对象。
     * @return 空分页对象。
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<>(new LinkedList<>(), 0L);
    }

    /**
     * 用户构建Mybatis Plus分页对象带有分页信息的数据列表。
     *
     * @param page   mybatis plus分页对象
     * @param targetClazz 模板对象类型。。
     * @param <T>         实体对象类型。
     * @param <D>         结果类型。
     * @return 返回分页数据对象。
     */
    public static <T, D> PageResult<D> makeResponseData(Page<T> page, Class<D> targetClazz) {
        List<T> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            // 这里需要构建分页数据对象，统一前端数据格式
            return PageResult.empty();
        }
        List<D> resultList = ModelUtil.copyCollectionTo(records, targetClazz);
        return makeResponseData(resultList, page.getTotal());
    }

    /**
     * 用户构建带有分页信息的数据列表。
     *
     * @param dataList   数据列表。
     * @param totalCount 总数量。
     * @param <T>        源数据类型。
     * @return 返回分页数据对象。
     */
    public static <T> PageResult<T> makeResponseData(List<T> dataList, Long totalCount) {
        PageResult<T> pageData = new PageResult<>();
        pageData.setRows(dataList);
        if (totalCount != null) {
            pageData.setTotal(totalCount);
        }
        return pageData;
    }
}