/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.controller;

import cn.hutool.core.collection.CollUtil;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictionaryVO;
import com.ctdi.cnos.llm.metadata.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 字典 控制层
 *
 * @author huangjinhua
 * @since 2024/4/23
 */
@Api(tags = {"字典操作接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @ApiOperation(value = "根据字典类型查询字典项列表", notes = "根据字典类型查询字典项列表")
    @GetMapping("/getDictListByDictType")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", required = true, paramType = "param")
    })
    public List<DictionaryVO> getDictListByDictType(@RequestParam(name = "dictType") String dictType) {
        return dictionaryService.getDictListByDictType(dictType);
    }
    @ApiOperation(value = "根据字典类型查询字典项列表封装成map(value:label)", notes = "根据字典类型查询字典项列表封装成map(value:label)")
    @GetMapping("/getDictItemMap")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", required = true, paramType = "param")
    })
    public Map<String, String> getDictItemMap(@RequestParam(name = "dictType") String dictType) {
        return dictionaryService.getDictItemMap(dictType);
    }

    @ApiOperation(value = "根据字典类型、字典项值查询字典项信息", notes = "根据字典类型、字典项值查询字典项信息")
    @GetMapping("/getDictItemInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", required = true, paramType = "param"),
            @ApiImplicitParam(name = "dictValue", value = "字典值", required = true, paramType = "param")
    })
    @AuthIgnore
    public DictionaryVO getDictByDictValue(@RequestParam(name = "dictType") String dictType,
                                           @RequestParam(name = "dictValue") String dictValue) {
        return dictionaryService.getDictItem(dictType, dictValue);
    }

    @ApiOperation(value = "根据字典类型、字典项值查询字典项标签", notes = "根据字典类型、字典项值查询字典项标签")
    @GetMapping("/getDictItemLabel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", required = true, paramType = "param"),
            @ApiImplicitParam(name = "dictValue", value = "字典值", required = true, paramType = "param")
    })
    public String getDictItemLabel(@RequestParam(name = "dictType") String dictType,
                                   @RequestParam(name = "dictValue") String dictValue) {
        return dictionaryService.getDictItemLabel(dictType, dictValue);
    }

    @ApiOperation(value = "根据字典类型更新字典缓存", notes = "根据字典类型更新字典缓存")
    @GetMapping("/updateDictCacheByDictType")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", required = true, paramType = "param")
    })
    public List<DictionaryVO> updateDictCacheByDictType(@RequestParam(name = "dictType") String dictType) {
        if (Objects.nonNull(dictType) ) {
            if ("all".equalsIgnoreCase(dictType)) {
                return dictionaryService.updateDictCacheByDictType(null);
            } else {
                return dictionaryService.updateDictCacheByDictType(dictType);
            }
        }
        return CollUtil.newArrayList();
    }
}
