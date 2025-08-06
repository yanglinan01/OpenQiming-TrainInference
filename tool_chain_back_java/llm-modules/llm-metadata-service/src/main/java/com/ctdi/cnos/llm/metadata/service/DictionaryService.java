package com.ctdi.cnos.llm.metadata.service;

import com.ctdi.cnos.llm.beans.meta.dictionary.DictData;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictType;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictionaryVO;

import java.util.List;
import java.util.Map;

/**
 * 字典 业务接口
 *
 * @author huangjinhua
 * @since 2024/4/23
 */
public interface DictionaryService {

    /**
     * 查询字典列表
     *
     * @param dictStatus    字典状态，类型表和字典项表同时生效
     * @param dictType      字典类型
     * @param dictId        字典类型ID
     * @param dictItemValue 字典项值
     * @return List<DictionaryVO>
     */
    List<DictionaryVO> queryList(String dictStatus, String dictType, String dictId, String dictItemValue);

    /**
     * 根据字典类型更新缓存
     *
     * @param dictType 字典类型
     * @return List<DictionaryVO>
     */
    List<DictionaryVO> updateDictCacheByDictType(String dictType);

    /**
     * 查询字典类型查询字典项列表
     *
     * @param dictType 字典类型
     * @return List<DictionaryVO>
     */
    List<DictionaryVO> getDictListByDictType(String dictType);

    /**
     * 根据字典类型查询字典项列表，并根据字典项键值作为key封装map
     *
     * @param dictType 字典类型
     * @return Map<String, DictionaryVO>
     */
    Map<String, DictionaryVO> getDictMap(String dictType);

    /**
     * 查询某个字典项
     *
     * @param dictType      字典类型
     * @param dictType      字典类型ID
     * @param dictItemValue 字典项值
     * @return DictionaryVO
     */
    DictionaryVO getDictItem(String dictType, String dictItemValue);


    /**
     * 查询字典,根据字典的value，label 并封装成value：label
     *
     * @param dictType 字典类型
     * @return Map<String, String>
     */
    Map<String, String> getDictItemMap(String dictType);

    /**
     * 根据字典类型和字典键值获取字典项的标签
     *
     * @param dictType      字典类型
     * @param dictItemValue 字典项键值
     * @return Map<String, String>
     */
    String getDictItemLabel(String dictType, String dictItemValue);

    /**
     * 新增字典类型
     *
     * @param dictType 字典类型对象
     * @return int
     */
    int insertDictType(DictType dictType);

    /**
     * 根据ID更新字典类型
     *
     * @param dictType 字典类型对象
     * @return int
     */
    int updateDictTypeById(DictType dictType);


    /**
     * 根据类型删除字典类型及字典项
     *
     * @param dictType 字典类型
     */
    void deleteDictType(String dictType);

    /**
     * 根据ID删除字典类型
     *
     * @param dictId 字典类型ID
     * @return int
     */
    int deleteDictTypeById(Long dictId);

    /**
     * 根据字典类型删除字典类型
     *
     * @param dictType 字典类型
     * @return int
     */
    int deleteDictTypeByDictType(String dictType);

    /**
     * 新增字典项
     *
     * @param dictData 字典项对象
     * @return int
     */
    int insertDictData(DictData dictData);

    /**
     * 根据ID更新字典项
     *
     * @param dictData 字典项对象
     * @return int
     */
    int updateDictDataById(DictData dictData);

    /**
     * 根据ID删除字典项
     *
     * @param dictCode 字典项ID
     * @return int
     */
    int deleteDictDataById(Long dictCode);

    /**
     * 根据字典类型批量删除字典项
     *
     * @param dictType 字典类型
     * @return int
     */
    int batchDeleteDictDataByDictType(String dictType);
}
