/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.controller.metadata;

import cn.hutool.core.map.MapUtil;
import com.ctdi.cnos.llm.beans.meta.prompt.Prompt;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptVO;
import com.ctdi.cnos.llm.feign.metadata.PromptServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * prompt 控制层
 *
 * @author huangjinhua
 * @since 2024/4/2
 */
@Api(tags = {"prompt操作接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/prompt")
@Slf4j
public class PromptController {

    private final PromptServiceClientFeign promptClient;

    @ApiOperation(value = "分页查询prompt列表", notes = "分页查询prompt列表")
    @GetMapping(value = "/queryPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页大小，默认为20", paramType = "param"),
            @ApiImplicitParam(name = "currentPage", value = "当前页，默认为1", paramType = "param"),
            @ApiImplicitParam(name = "name", value = "prompt名称", paramType = "param"),
            @ApiImplicitParam(name = "belong", value = "prompt归属", required = true, paramType = "param"),
            @ApiImplicitParam(name = "types", value = "prompt类别,多个以逗号（,）分隔", paramType = "param")
    })
    public OperateResult<Map<String, Object>> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                                        @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                                        @RequestParam(name = "name", required = false) String name,
                                                        @RequestParam(name = "belong") String belong,
                                                        @RequestParam(name = "types", required = false) String types) {
        try {
            //0表示查询所有分类
            if ("0".equals(types)) {
                types = null;
            }
            Map<String, Object> result = promptClient.queryPage(pageSize, currentPage, name, belong, types);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("分页查询prompt列表异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "查询prompt列表", notes = "查询prompt列表")
    @GetMapping(value = "/queryList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "prompt名称", paramType = "param"),
            @ApiImplicitParam(name = "belong", value = "prompt归属", required = true, paramType = "param"),
            @ApiImplicitParam(name = "types", value = "prompt类别,多个以逗号（,）分隔", paramType = "param")
    })
    public OperateResult<List<PromptVO>> queryList(@RequestParam(name = "name", required = false) String name,
                                                   @RequestParam(name = "belong") String belong,
                                                   @RequestParam(name = "types", required = false) String types) {
        try {
            //0表示查询所有分类
            if ("0".equals(types)) {
                types = null;
            }
            List<PromptVO> result = promptClient.queryList(name, belong, types);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("查询prompt列表异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "查询prompt详情", notes = "查询prompt详情")
    @GetMapping(value = "/detail")
    public OperateResult<PromptVO> detail(@RequestParam("id") Long id) {
        try {
            PromptVO result = promptClient.detail(id);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("查询prompt详情异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "新增prompt", notes = "新增prompt")
    @PostMapping("/add")
    public OperateResult<Void> add(@RequestBody Prompt prompt) {
        try {
            Map<String, Object> result = promptClient.add(prompt);
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("新增prompt异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "根据 prompt 内容获取变量标识符", notes = "根据 prompt 内容获取变量标识符")
    @PostMapping("/getIdentifier")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "promptText", value = "prompt 内容", required = true, paramType = "body")
    })
    public OperateResult<String> getIdentifier(@ApiIgnore @RequestBody String promptText) {
        try {
            String result = promptClient.getIdentifier(promptText);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("prompt 内容获取变量标识符异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "根据 prompt 内容获取变量", notes = "根据 prompt 内容获取变量")
    @PostMapping("/getVariable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "promptText", value = "prompt 内容", required = true, paramType = "body")
    })
    public OperateResult<List<String>> getVariable(@ApiIgnore @RequestBody String promptText) {
        try {
            List<String> result = promptClient.getVariable(promptText);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("prompt 内容获取变量标识符异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "修改prompt", notes = "修改prompt")
    @PostMapping("/updateById")
    public OperateResult<Void> update(@RequestBody Prompt prompt) {
        try {
            Map<String, Object> result = promptClient.update(prompt);
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("修改prompt异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "删除prompt", notes = "删除prompt")
    @DeleteMapping("/deleteById")
    public OperateResult<Void> deleteById(@RequestParam("id") Long id) {
        try {
            Map<String, Object> result = promptClient.deleteById(id);
            return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
        } catch (Exception exception) {
            log.error("删除prompt异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }
}
