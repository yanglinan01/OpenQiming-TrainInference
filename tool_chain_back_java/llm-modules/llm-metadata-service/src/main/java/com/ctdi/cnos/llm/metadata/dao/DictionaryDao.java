/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.beans.meta.dictionary.DictData;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictType;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictionaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典 Dao
 *
 * @author huangjinhua
 * @since 2024/4/23
 */
@Mapper
public interface DictionaryDao {

    /**
     * 查询字典列表
     *
     * @param dictStatus    字典状态，类型表和字典项表同时生效
     * @param dictType      字典类型
     * @param dictId        字典类型ID
     * @param dictItemValue 字典项值
     * @return
     */
    List<DictionaryVO> queryList(@Param("dictStatus") String dictStatus, @Param("dictType") String dictType, @Param("dictId") String dictId, @Param("dictItemValue") String dictItemValue);

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
     * 根据字典类型删除字典类型
     *
     * @param dictType 字典类型
     * @return int
     */
    int deleteDictTypeByDictType(@Param("dictType") String dictType);

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
     * 根据字典类型批量删除字典项
     *
     * @param dictType 字典类型
     * @return
     */
    int batchDeleteDictDataByDictType(@Param("dictType") String dictType);

}
