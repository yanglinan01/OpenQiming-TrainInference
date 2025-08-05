package com.ctdi.cnos.llm.base.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.ctdi.cnos.llm.base.annotation.RelationGlobalDict;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.base.mapper.BaseModelMapper;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.SortingField;
import com.ctdi.cnos.llm.util.ModelUtil;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.MPJWrappers;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * 所有Service的基类。
 *
 * @param <T> 主Model实体对象。
 * @param <V> DomainVO域对象。
 * @author laiqi
 * @since 2024/7/12
 */
public abstract class BaseService<M extends BaseDaoMapper<T>, T, V> extends MPJBaseServiceImpl<M, T> implements IBaseService<T, V> {

    /**
     * 当前Service关联的主model的VO对象的Class。
     */
    protected final Class<V> domainVoClass = currentDomainVoClass();

    /**
     * 实体对应的表信息。
     */
    protected final TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);

    /**
     * 实体对应的字段信息，该字段在系统启动阶段一次性预加载，提升运行时效率。。
     */
    protected Map<String, TableFieldInfo> entityFieldInfoMap = new HashMap<>();

    /**
     * 全局字典对象的字段，该字段在系统启动阶段一次性预加载，提升运行时效率。
     */
    protected final List<Field> relationGlobalDictList = new LinkedList<>();

    public BaseService() {
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        // 为了覆盖继承情况. 需要采用倒序遍历(即优先处理父类的, 子类的属性可以覆盖)
        for (int i = fieldList.size() - 1; i >= 0; i--) {
            TableFieldInfo tableFieldInfo = fieldList.get(i);
            entityFieldInfoMap.put(tableFieldInfo.getProperty(), tableFieldInfo);
        }

        Field[] fields = ReflectUtil.getFields(getEntityClass());
        for (Field field : fields) {
            RelationGlobalDict annotation = field.getAnnotation(RelationGlobalDict.class);
            if (null != annotation) {
                relationGlobalDictList.add(field);
            }
        }
    }

    @Override
    public LambdaQueryWrapperX<T> getLambdaQueryWrapper(QueryParam queryParam) {
        LambdaQueryWrapperX<T> lambdaQueryWrapper = new LambdaQueryWrapperX<>();
        if(queryParam != null){
            // 排序字段(统一在这里定义。不要放在Page分页对象中里面排序)
            List<SortingField> sortingFields = queryParam.getSortingFields();
            if (CollUtil.isNotEmpty(sortingFields)) {
                for (SortingField sortingField : sortingFields) {
                    // 普通字段的排序
                    TableFieldInfo tableFieldInfo = entityFieldInfoMap.get(sortingField.getFieldName());
                    if (Objects.nonNull(tableFieldInfo)) {
                        lambdaQueryWrapper.orderBy(sortingField.getAsc(), tableFieldInfo.getColumn());
                        continue;
                    }
                    // 主键的排序
                    if (StrUtil.equalsIgnoreCase(tableInfo.getKeyProperty(), sortingField.getFieldName())) {
                        lambdaQueryWrapper.orderBy(sortingField.getAsc(), tableInfo.getKeyColumn());
                    }
                }
            }

            // 处理个性化的查询条件组装。
            configureQueryWrapper(lambdaQueryWrapper, queryParam);
        }
        return lambdaQueryWrapper;
    }

    @Override
    public MPJBaseJoin<T> configureJoinWrapper(QueryParam queryParam) {
        return MPJWrappers.lambdaJoin(this.entityClass);
    }

    @Override
    public PageResult<V> queryPage(QueryParam queryParam) {
        PageResult<T> pageResult = baseMapper.selectPage(queryParam.getPageParam(), getLambdaQueryWrapper(queryParam));
        this.buildRelationForDataList(pageResult.getRows(), null, queryParam.getWithDict());
        List<V> voList = convertToVoList(pageResult.getRows(), null);
        return PageResult.makeResponseData(voList, pageResult.getTotal());
    }

    @Override
    public List<V> queryList(QueryParam queryParam) {
        List<T> records = baseMapper.selectList(getLambdaQueryWrapper(queryParam));
        this.buildRelationForDataList(records, null, queryParam.getWithDict());
        return convertToVoList(records, null);
    }

    @Override
    public V queryById(Serializable id, Boolean withDict) {
        T entity = baseMapper.selectById(id);
        buildRelationForData(entity, null, withDict);
        return convertToVo(entity, null);
    }

    @Override
    public boolean deleteById(Serializable id) {
        return baseMapper.deleteById(id) == 1;
    }

    @Override
    public boolean save(T entity) {
        // ModelUtil.fillCommonsForInsert(entity);
        return super.save(entity);
    }

    @Override
    public boolean updateById(T entity) {
        // ModelUtil.fillCommonsForUpdate(entity);
        return super.updateById(entity);
    }

    @Override
    public void buildRelationForDataList(List<T> resultList, Set<String> ignoreFields, Boolean withDict) {
        if (CollUtil.isEmpty(resultList)) {
            return;
        }
        // 构建全局字典关联关系
        if (BooleanUtil.isTrue(withDict)) {
            for (Field field : relationGlobalDictList) {
                RelationGlobalDict annotation = field.getAnnotation(RelationGlobalDict.class);
                if (ignoreFields != null && ignoreFields.contains(field.getName())) {
                    continue;
                }
                String masterIdField = annotation.masterIdField();
                Set<Object> masterIdSet = resultList.stream()
                        .map(obj -> ReflectUtil.getFieldValue(obj, masterIdField))
                        .filter(Objects::nonNull)
                        .collect(toSet());
                if (CollUtil.isNotEmpty(masterIdSet)) {
                    String dictCode = annotation.dictCode();
                    Object dictionaryService = SpringUtil.getBean(ApplicationConstant.DICTIONARY_SERVICE_CLIENT_NAME);
                    Method getDictItemMap = ReflectUtil.getMethodByName(dictionaryService.getClass(), ApplicationConstant.DICTIONARY_SERVICE_CLIENT_METHOD_NAME);
                    Map<String, String> dictMap = ReflectUtil.invoke(dictionaryService, getDictItemMap, dictCode);
                    resultList.forEach(entity -> {
                        if (Objects.nonNull(entity)) {
                            Object id = ReflectUtil.getFieldValue(entity, masterIdField);
                            if (Objects.nonNull(id)) {
                                String name = dictMap.get(id.toString());
                                if (name != null) {
                                    Map<String, Object> m = new HashMap<>(2);
                                    m.put("id", id);
                                    m.put("name", name);
                                    ReflectUtil.setFieldValue(entity, field, m);
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void buildRelationForData(T dataObject, Set<String> ignoreFields, Boolean withDict) {
        if (dataObject == null) {
            return;
        }
        // 构建全局字典关联关系
        if (BooleanUtil.isTrue(withDict)) {
            for (Field field : relationGlobalDictList) {
                RelationGlobalDict annotation = field.getAnnotation(RelationGlobalDict.class);
                if (ignoreFields != null && ignoreFields.contains(field.getName())) {
                    continue;
                }
                String masterIdField = annotation.masterIdField();
                Object id = ReflectUtil.getFieldValue(dataObject, masterIdField);
                if (Objects.nonNull(id)) {
                    String dictCode = annotation.dictCode();
                    Object dictionaryService = SpringUtil.getBean(ApplicationConstant.DICTIONARY_SERVICE_CLIENT_NAME);
                    Method getDictItemMap = ReflectUtil.getMethodByName(dictionaryService.getClass(), ApplicationConstant.DICTIONARY_SERVICE_CLIENT_METHOD_NAME);
                    Map<String, String> dictMap = ReflectUtil.invoke(dictionaryService, getDictItemMap, dictCode);
                    String name = dictMap.get(id.toString());
                    if (name != null) {
                        Map<String, Object> reulstDictMap = new HashMap<>(2);
                        reulstDictMap.put("id", id);
                        reulstDictMap.put("name", name);
                        ReflectUtil.setFieldValue(dataObject, field, reulstDictMap);
                    }
                }
            }
        }
    }

    /**
     * 将Model实体对象的集合转换为DomainVO域对象的集合。
     * 如果Model存在该实体的ModelMapper，就用该ModelMapper转换，否则使用缺省的基于字段反射的copy。
     *
     * @param modelList   实体对象列表。
     * @param modelMapper 从实体对象到VO对象的映射对象。
     * @return 转换后的VO域对象列表。
     */
    protected List<V> convertToVoList(List<T> modelList, BaseModelMapper<V, T> modelMapper) {
        List<V> resultVoList;
        if (modelMapper != null) {
            resultVoList = modelMapper.fromModelList(modelList);
        } else {
            resultVoList = ModelUtil.copyCollectionTo(modelList, domainVoClass);
        }
        return resultVoList;
    }

    /**
     * 将Model实体对象转换为DomainVO域对象。
     * 如果Model存在该实体的ModelMapper，就用该ModelMapper转换，否则使用缺省的基于字段反射的copy。
     *
     * @param model       实体对象。
     * @param modelMapper 从实体对象到VO对象的映射对象。
     * @return 转换后的VO域对象。
     */
    protected V convertToVo(T model, BaseModelMapper<V, T> modelMapper) {
        V resultVo;
        if (modelMapper != null) {
            resultVo = modelMapper.fromModel(model);
        } else {
            resultVo = ModelUtil.copyTo(model, domainVoClass);
        }
        return resultVo;
    }

    @SuppressWarnings("unchecked")
    protected Class<V> currentDomainVoClass() {
        return (Class<V>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseService.class, 2);
    }
}