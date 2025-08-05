package com.ctdi.cnos.llm.feign.metadata;


import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictionaryVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 字典 OpenFeign
 *
 * @author huangjinhua
 * @since 2024/4/23
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface DictionaryServiceClientFeign {

    /**
     * 根据字典类型查询字典项列表
     *
     * @param dictType 字典类型
     * @return List<DictionaryVO>
     */
    @GetMapping("/dictionary/getDictListByDictType")
    List<DictionaryVO> getDictListByDictType(@RequestParam(name = "dictType") String dictType);

    /**
     * 根据字典类型查询字典项列表封装成map(value:label)
     *
     * @param dictType
     * @return
     */
    @GetMapping("/dictionary/getDictItemMap")
    Map<String, String> getDictItemMap(@RequestParam(name = "dictType") String dictType);

    /**
     * 根据字典类型、字典项值查询字典项信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典状态
     * @return DictionaryVO
     */
    @GetMapping("/dictionary/getDictItemInfo")
    DictionaryVO getDictItemInfo(@RequestParam(name = "dictType") String dictType,
                                 @RequestParam(name = "dictValue") String dictValue);

    /**
     * 根据字典类型、字典项值查询字典项标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典状态
     * @return DictionaryVO
     */
    @GetMapping("/dictionary/getDictItemLabel")
    String getDictItemLabel(@RequestParam(name = "dictType") String dictType,
                            @RequestParam(name = "dictValue") String dictValue);

    /**
     * 根据字典类型更新字典缓存
     *
     * @param dictType 字典类型
     * @return List<DictionaryVO>
     */
    @GetMapping("/dictionary/updateDictCacheByDictType")
    List<DictionaryVO> updateDictCacheByDictType(@RequestParam(name = "dictType") String dictType);
}
