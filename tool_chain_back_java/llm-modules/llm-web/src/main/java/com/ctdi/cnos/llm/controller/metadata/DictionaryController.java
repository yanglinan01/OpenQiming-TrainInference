/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.controller.metadata;

import com.ctdi.cnos.llm.beans.meta.dictionary.DictionaryVO;
import com.ctdi.cnos.llm.feign.metadata.DictionaryServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典 控制层
 *
 * @author huangjinhua
 * @since 2024/4/23
 */
@Api(tags = {"字典操作接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/dictionary")
@Slf4j
public class DictionaryController {

    private final DictionaryServiceClientFeign dictionaryClient;

    @ApiOperation(value = "根据字典类型查询字典项列表", notes = "根据字典类型查询字典项列表")
    @GetMapping("/getDictListByDictType")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", required = true, paramType = "param")
    })
    public OperateResult<List<DictionaryVO>> getDictListByDictType(@RequestParam(name = "dictType") String dictType) {
        try {
            List<DictionaryVO> result = dictionaryClient.getDictListByDictType(dictType);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("根据字典类型查询字典项列表异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }


    @ApiOperation(value = "根据字典类型、字典项值查询字典项信息", notes = "根据字典类型、字典项值查询字典项信息")
    @GetMapping("/getDictItemInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", required = true, paramType = "param"),
            @ApiImplicitParam(name = "dictValue", value = "字典值", required = true, paramType = "param")
    })
    public OperateResult<DictionaryVO> getDictItemInfo(@RequestParam(name = "dictType") String dictType,
                                                       @RequestParam(name = "dictValue") String dictValue) {
        try {
            DictionaryVO result = dictionaryClient.getDictItemInfo(dictType, dictValue);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("根据字典类型、字典项值查询字典项信息异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "根据字典类型、字典项值查询字典项标签", notes = "根据字典类型、字典项值查询字典项标签")
    @GetMapping("/getDictItemLabel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", required = true, paramType = "param"),
            @ApiImplicitParam(name = "dictValue", value = "字典值", required = true, paramType = "param")
    })
    public OperateResult<String> getDictItemLabel(@RequestParam(name = "dictType") String dictType,
                                                  @RequestParam(name = "dictValue") String dictValue) {
        try {
            String result = dictionaryClient.getDictItemLabel(dictType, dictValue);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("根据字典类型、字典项值查询字典项信息标签", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }


    @ApiOperation(value = "根据字典类型更新字典缓存", notes = "根据字典类型更新字典缓存")
    @GetMapping("/updateDictCacheByDictType")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", required = true, paramType = "param")
    })
    public OperateResult<List<DictionaryVO>> updateDictCacheByDictType(@RequestParam(name = "dictType") String dictType) {
        try {
            List<DictionaryVO> result = dictionaryClient.updateDictCacheByDictType(dictType);
            return new OperateResult<>(true, "更新完成", result);
        } catch (Exception exception) {
            log.error("根据字典类型更新字典缓存", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }
}
