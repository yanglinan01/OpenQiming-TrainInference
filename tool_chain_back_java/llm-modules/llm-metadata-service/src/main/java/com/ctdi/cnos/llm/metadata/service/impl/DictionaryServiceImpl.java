/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictData;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictType;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictionaryVO;
import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import com.ctdi.cnos.llm.metadata.dao.DictionaryDao;
import com.ctdi.cnos.llm.metadata.service.DictionaryService;
import com.ctdi.cnos.llm.base.constant.CacheConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * prompt 业务接口实现
 *
 * @author huangjinhua
 * @since 2024/4/2
 */
@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {
    private final DictionaryDao dictionaryDao;

    private final CtgCache ctgCache;

    @Override
    public List<DictionaryVO> queryList(String dictStatus, String dictType, String dictId, String dictItemValue) {
        return dictionaryDao.queryList(dictStatus, dictType, dictId, dictItemValue);
    }

    @Override
    public List<DictionaryVO> updateDictCacheByDictType(String dictType) {
        //清除缓存
        this.clearDictCache(dictType);
        //查询数据
        List<DictionaryVO> list = this.queryList(MetaDataConstants.DICT_STATUS_IS_VALID_TRUE, dictType, null, null);
        //设置缓存
        if (CollUtil.isNotEmpty(list)) {
            //全刷新字典缓存
            if (dictType == null) {
                Map<String, List<DictionaryVO>> dicTypeMap = list.stream().collect(Collectors.groupingBy(DictionaryVO::getDictType));
                dicTypeMap.forEach((type, value) -> ctgCache.set(CacheConstant.DICTIONARY_CACHE_NAME, type, value, CacheConstant.CACHE_EXPIRE_1_DAY));
            } else {
                ctgCache.set(CacheConstant.DICTIONARY_CACHE_NAME, dictType, list, CacheConstant.CACHE_EXPIRE_1_DAY);
            }
        }
        return list;
    }


    @Override
    public List<DictionaryVO> getDictListByDictType(String dictType) {
        //查询缓存
        List<DictionaryVO> list = ctgCache.getCache(CacheConstant.DICTIONARY_CACHE_NAME, dictType);
        if (CollUtil.isNotEmpty(list)) {
            return list;
        }
        list = this.updateDictCacheByDictType(dictType);
        return Optional.ofNullable(list).orElse(CollUtil.newArrayList());
    }

    private void clearDictCache(String dicType) {
        if (Objects.isNull(dicType)) {
            ctgCache.clear(CacheConstant.DICTIONARY_CACHE_NAME);
        } else {
            ctgCache.remove(CacheConstant.DICTIONARY_CACHE_NAME, dicType);
        }
    }

    @Override
    public Map<String, DictionaryVO> getDictMap(String dictType) {
        List<DictionaryVO> list = this.getDictListByDictType(dictType);
        return Optional.ofNullable(list).map(value -> list.stream().collect(Collectors.toMap(DictionaryVO::getDictItemValue, Function.identity()))).orElse(MapUtil.empty());
    }

    @Override
    public DictionaryVO getDictItem(String dictType, String dictItemValue) {
        if (Objects.isNull(dictType) || Objects.isNull(dictItemValue)) {
            return null;
        }
        DictionaryVO dictionaryVO = this.getDictMap(dictType).get(dictItemValue);
        if (dictionaryVO == null) {
            this.updateDictCacheByDictType(dictType);
        }
        return this.getDictMap(dictType).get(dictItemValue);
    }

    @Override
    public Map<String, String> getDictItemMap(String dictType) {
        if (Objects.isNull(dictType)) {
            return MapUtil.empty();
        }
        List<DictionaryVO> list = this.getDictListByDictType(dictType);
        return Optional.ofNullable(list)
                .map(value -> list.stream().collect(Collectors.toMap(DictionaryVO::getDictItemValue, DictionaryVO::getDictItemLabel)))
                .orElse(MapUtil.empty());
    }

    @Override
    public String getDictItemLabel(String dictType, String dictItemValue) {
        if (Objects.isNull(dictType) || Objects.isNull(dictItemValue)) {
            return null;
        }
        DictionaryVO vo = this.getDictMap(dictType).get(dictItemValue);
        return Optional.ofNullable(vo).map(value -> vo.getDictItemLabel()).orElse(null);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertDictType(DictType dictType) {
        if (Objects.isNull(dictType.getDictId())) {
            dictType.setDictId(IdUtil.getSnowflakeNextId());
        }
        return dictionaryDao.insertDictType(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDictTypeById(DictType dictType) {
        if (Objects.isNull(dictType.getDictId())) {
            return 0;
        }
        return dictionaryDao.updateDictTypeById(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictType(String dictType) {
        if (Objects.isNull(dictType)) {
            return;
        }
        //清除字典缓存
        this.clearDictCache(dictType);
        //删除字典类型
        dictionaryDao.deleteDictTypeByDictType(dictType);
        //删除字典项
        dictionaryDao.batchDeleteDictDataByDictType(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteDictTypeById(Long dictId) {
        if (Objects.isNull(dictId)) {
            return 0;
        }
        DictType dictType = new DictType();
        dictType.setStatus(MetaDataConstants.DICT_STATUS_IS_VALID_FALSE);
        dictType.setDictId(dictId);
        return dictionaryDao.updateDictTypeById(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteDictTypeByDictType(String dictType) {
        if (Objects.isNull(dictType)) {
            return 0;
        }
        return dictionaryDao.deleteDictTypeByDictType(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertDictData(DictData dictData) {
        if (Objects.isNull(dictData.getDictCode())) {
            dictData.setDictCode(IdUtil.getSnowflakeNextId());
        }
        return dictionaryDao.insertDictData(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDictDataById(DictData dictData) {
        if (Objects.isNull(dictData.getDictCode())) {
            return 0;
        }
        return dictionaryDao.updateDictDataById(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteDictDataById(Long dictCode) {
        if (Objects.isNull(dictCode)) {
            return 0;
        }
        DictData dictData = new DictData();
        dictData.setStatus(MetaDataConstants.DICT_STATUS_IS_VALID_FALSE);
        dictData.setDictCode(dictCode);
        return dictionaryDao.updateDictDataById(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteDictDataByDictType(String dictType) {
        if (Objects.isNull(dictType)) {
            return 0;
        }
        this.clearDictCache(dictType);
        return dictionaryDao.batchDeleteDictDataByDictType(dictType);
    }
}