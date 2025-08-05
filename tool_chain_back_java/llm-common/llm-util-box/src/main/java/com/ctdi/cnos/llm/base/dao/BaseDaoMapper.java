package com.ctdi.cnos.llm.base.dao;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.base.object.PageParam;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.SortingField;
import com.ctdi.cnos.llm.util.MybatisPlusUtil;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 数据访问对象的基类。
 *
 * @param <T> 主Model实体对象。
 * @author laiqi
 * @since 2024/7/12
 * @see <a href="https://mybatisplusjoin.com/">MyBatis-Plus-Join</a>
 */
public interface BaseDaoMapper<T> extends MPJBaseMapper<T> {
    /**
     * 查询单条记录
     * @param field
     * @param value
     * @return
     */
    default T selectOne(String field, Object value) {
        return selectOne(new QueryWrapper<T>().eq(field, value));
    }

    /**
     * 查询单条记录
     * @param field
     * @param value
     * @return
     */
    default T selectOne(SFunction<T, ?> field, Object value) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field, value));
    }

    /**
     * 查询单条记录
     * @param field1
     * @param value1
     * @param field2
     * @param value2
     * @return
     */
    default T selectOne(String field1, Object value1, String field2, Object value2) {
        return selectOne(new QueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    /**
     * 查询单条记录
     * @param field1
     * @param value1
     * @param field2
     * @param value2
     * @return
     */
    default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    /**
     * 判断是否存在
     * @param field
     * @param value
     * @return
     */
   default boolean existOne(String field, Object value) {
        return selectCount(field, value) == 1;
   }

    /**
     * 分页查询(排序请放Wrapper中添加)
     * @param pageParam
     * @param queryWrapper
     * @return
     */
    default PageResult<T> selectPage(PageParam pageParam, @Param("ew") Wrapper<T> queryWrapper) {
        // 特殊：不分页，直接查询全部
        if (PageParam.PAGE_SIZE_NONE.equals(pageParam.getPageSize())) {
            List<T> list = selectList(queryWrapper);
            return new PageResult<>(list, (long) list.size());
        }
        // 页码 + 数量
        Page<T> page = MybatisPlusUtil.buildPage(pageParam);
        selectPage(page, queryWrapper);
        // 转换返回
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    /**
     * 统计数量
     * @param filter
     * @return
     */
    default Long selectCount(T filter) {
        return selectCount(new QueryWrapper<>(filter));
    }

    /**
     * 统计数量
     * @param field
     * @param value
     * @return
     */
    default Long selectCount(String field, Object value) {
        return selectCount(new QueryWrapper<T>().eq(field, value));
    }

    /**
     * 统计数量
     * @param field
     * @param value
     * @return
     */
    default Long selectCount(SFunction<T, ?> field, Object value) {
        return selectCount(new LambdaQueryWrapper<T>().eq(field, value));
    }

    /**
     * 查询列表
     * @return
     */
    default List<T> selectList() {
        return selectList(new QueryWrapper<>());
    }

    /**
     * 查询列表
     * @param field
     * @param value
     * @return
     */
    default List<T> selectList(String field, Object value) {
        return selectList(new QueryWrapper<T>().eq(field, value));
    }

    /**
     * 查询列表
     * @param field
     * @param value
     * @return
     */
    default List<T> selectList(SFunction<T, ?> field, Object value) {
        return selectList(new LambdaQueryWrapper<T>().eq(field, value));
    }

    /**
     * 查询列表
     * @param field
     * @param values
     * @return
     */
    default List<T> selectList(String field, Collection<?> values) {
        if (CollUtil.isEmpty(values)) {
            return CollUtil.newArrayList();
        }
        return selectList(new QueryWrapper<T>().in(field, values));
    }

    /**
     * 查询列表
     * @param field
     * @param values
     * @return
     */
    default List<T> selectList(SFunction<T, ?> field, Collection<?> values) {
        if (CollUtil.isEmpty(values)) {
            return CollUtil.newArrayList();
        }
        return selectList(new LambdaQueryWrapper<T>().in(field, values));
    }

    /**
     * 查询列表
     * @param leField
     * @param geField
     * @param value
     * @return
     */
    default List<T> selectList(SFunction<T, ?> leField, SFunction<T, ?> geField, Object value) {
        return selectList(new LambdaQueryWrapper<T>().le(leField, value).ge(geField, value));
    }

    /**
     * 查询列表
     * @param field1
     * @param value1
     * @param field2
     * @param value2
     * @return
     */

    default List<T> selectList(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectList(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    /**
     * 批量更新
     * @param update
     * @return
     */

    default int updateBatch(T update) {
        return update(update, new QueryWrapper<>());
    }

    /**
     * 删除
     * @param field
     * @param value
     * @return
     */
    default int delete(String field, String value) {
        return delete(new QueryWrapper<T>().eq(field, value));
    }

    /**
     * 删除
     * @param field
     * @param value
     * @return
     */
    default int delete(SFunction<T, ?> field, Object value) {
        return delete(new LambdaQueryWrapper<T>().eq(field, value));
    }

    /**
     * 删除
     * @param filter
     * @return
     */
    default int delete(T filter) {
        return delete(new QueryWrapper<>(filter));
    }
}