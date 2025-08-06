package com.ctdi.cnos.llm.base.service;

import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.github.yulichang.base.MPJBaseService;
import com.github.yulichang.interfaces.MPJBaseJoin;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 所有Service的接口。
 *
 * @param <T> 主Model实体对象。
 * @author laiqi
 * @since 2024/7/12
 */
public interface IBaseService<T, V> extends MPJBaseService<T> {

    /**
     * 获取查询条件。供分页查询和列表\查询中使用。
     * 提供如下功能：
     * 1、统一处理排序字段；
     * @param queryParam 查询条件对象。
     * @return 查询条件。
     */
    LambdaQueryWrapperX<T> getLambdaQueryWrapper(QueryParam queryParam);


    /**
     * 个性化配置查询条件。
     * @param wrapper 已经处理排序的查询条件。
     * @param queryParam 查询参数
     */
    void configureQueryWrapper(LambdaQueryWrapperX<T> wrapper, QueryParam queryParam);

    /**
     * 配置关联查询。
     * 自定义完整查询语句，包括排序等。
     * @param queryParam 查询参数
     * @return 关联Wrapper对象。
     */
    MPJBaseJoin<T> configureJoinWrapper(QueryParam queryParam);

    /**
     * 分页查询。
     * @param queryParam
     * @return
     */
    PageResult<V> queryPage(QueryParam queryParam);

    /**
     * 列表查询。
     * @param queryParam
     * @return
     */
    List<V> queryList(QueryParam queryParam);

    /**
     * 根据主键查询。
     * @param id
     * @param withDict
     * @return
     */
    V queryById(Serializable id, Boolean withDict);

    /**
     * 根据主键删除。
     * @param id
     * @return
     */
    boolean deleteById(Serializable id);

    /**
     * 批量构建关联数据。
     * @param resultList
     * @param ignoreFields
     * @param withDict
     */
    void buildRelationForDataList(List<T> resultList, Set<String> ignoreFields, Boolean withDict);

    /**
     * 构建关联数据。
     * @param dataObject
     * @param ignoreFields
     * @param withDict
     */
    void buildRelationForData(T dataObject, Set<String> ignoreFields, Boolean withDict);
}