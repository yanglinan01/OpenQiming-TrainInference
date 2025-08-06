package com.ctdi.cnos.llm.util;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.PageParam;
import com.ctdi.cnos.llm.base.object.SortingField;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MybatisPlus 工具类。
 *
 * @author laiqi
 * @since 2024/7/12
 */
public class MybatisPlusUtil {

    /**
     * 构建分页对象
     *
     * @param pageParam 分页请求对象
     * @param <T>       实体类型。
     * @return 分页对象。
     */
    public static <T> Page<T> buildPage(PageParam pageParam) {
        return buildPage(pageParam, null);
    }

    /**
     * 构建分页对象(不推荐在分页中设置排序。放在QueryWrapper中可以复用同时支持分页和列表查询)
     *
     * @param pageParam     分页请求对象
     * @param sortingFields 排序字段对象集合。
     * @param <T>           实体类型。
     * @return 分页对象。
     */
    public static <T> Page<T> buildPage(PageParam pageParam, Collection<SortingField> sortingFields) {
        // 页码 + 数量
        Page<T> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        // 排序字段
        if (!CollUtil.isEmpty(sortingFields)) {
            page.addOrder(sortingFields.stream().map(sortingField -> sortingField.getAsc() ?
                            OrderItem.asc(sortingField.getFieldName()) : OrderItem.desc(sortingField.getFieldName()))
                    .collect(Collectors.toList()));
        }
        return page;
    }

    /**
     * 添加排序字段到查询条件。
     *
     * @param queryWrapper  查询条件。
     * @param sortingFields 排序字段对象集合。
     * @param <T>           实体类型。
     */
    public static <T> void addQueryWrapperOrder(LambdaQueryWrapperX<T> queryWrapper, TableInfo tableInfo, Collection<SortingField> sortingFields) {
        if (queryWrapper == null) {
            return;
        }
        // 排序字段
        if (!CollUtil.isEmpty(sortingFields)) {
            sortingFields.forEach(sortingField -> {
                tableInfo.getFieldList().stream().filter(field -> field.getProperty().equals(sortingField.getFieldName()))
                        .findFirst().ifPresent(tableFieldInfo -> {
                            queryWrapper.orderBy(sortingField.getAsc(), tableFieldInfo.getColumn());
                        });
            });
        }
    }

    /**
     * 获得 Table 对应的表名
     * <p>
     * 模式.表名
     *
     * @param table 表
     * @return 去除转移字符后的表名
     */
    public static String getTableName(Table table) {
        return table.getSchemaName() + StringPool.DOT + table.getName();
    }

    /**
     * 构建 Column 对象
     *
     * @param tableName  表名
     * @param tableAlias 别名
     * @param column     字段名
     * @return Column 对象
     */
    public static Column buildColumn(String tableName, Alias tableAlias, String column) {
        if (tableAlias != null) {
            return new Column(tableAlias.getName() + StringPool.DOT + column);
        }
        return new Column(column);
    }

    /**
     * 复制过来的判断方法。
     * @see com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper
     * @param node
     * @param name
     * @param value
     * @return
     */
    public static Boolean getBoolean(String node, String name, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        if (StringPool.ONE.equals(value) || StringPool.TRUE.equals(value) || StringPool.ON.equals(value)) {
            return true;
        }
        if (StringPool.ZERO.equals(value) || StringPool.FALSE.equals(value) || StringPool.OFF.equals(value)) {
            return false;
        }
        throw ExceptionUtils.mpe("unsupported value \"%s\" by `@InterceptorIgnore#%s` on top of \"%s\"", value, node, name);
    }

    /**
     * 将拦截器添加到链中
     * 由于 MybatisPlusInterceptor 不支持添加拦截器，所以只能全量设置
     *
     * @param interceptor 链
     * @param inner       拦截器
     * @param index       位置
     */
    public static void addInterceptor(MybatisPlusInterceptor interceptor, InnerInterceptor inner, int index) {
        List<InnerInterceptor> inners = new ArrayList<>(interceptor.getInterceptors());
        inners.add(index, inner);
        interceptor.setInterceptors(inners);
    }


    private MybatisPlusUtil() {
    }
}