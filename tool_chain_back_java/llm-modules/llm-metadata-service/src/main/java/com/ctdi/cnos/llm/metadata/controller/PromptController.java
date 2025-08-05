/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.prompt.Prompt;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptVO;
import com.ctdi.cnos.llm.metadata.service.PromptService;
import com.ctdi.cnos.llm.util.CommonResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * prompt 控制层
 *
 * @author huangjinhua
 * @since 2024/4/2
 */
@Api(tags = {"prompt操作接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/prompt")
public class PromptController {

    private final PromptService promptService;

    @ApiOperation(value = "分页查询prompt列表", notes = "分页查询prompt列表")
    @GetMapping("/queryPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页大小，默认为20", paramType = "param"),
            @ApiImplicitParam(name = "currentPage", value = "当前页，默认为1", paramType = "param"),
            @ApiImplicitParam(name = "name", value = "prompt名称", paramType = "param"),
            @ApiImplicitParam(name = "belong", value = "prompt归属", required = true, paramType = "param"),
            @ApiImplicitParam(name = "types", value = "prompt类别,多个以逗号（,）分隔", paramType = "param")
    })
    public Map<String, Object> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                         @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                         @RequestParam(name = "name", required = false) String name,
                                         @RequestParam(name = "belong") String belong,
                                         @RequestParam(name = "types", required = false) String types) {
        Page<PromptVO> page = new Page<>(currentPage, pageSize);
        page.addOrder(OrderItem.desc("modify_date"));
        PromptVO prompt = new PromptVO();
        prompt.setName(name);
        prompt.setBelong(belong);
        prompt.setTypes(types);
        promptService.queryList(page, prompt);
        Map<String, Object> result = new HashMap<>(2);
        result.put("total", page.getTotal());
        result.put("rows", page.getRecords());
        return result;
    }

    @ApiOperation(value = "查询prompt列表", notes = "查询prompt列表")
    @GetMapping("/queryList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "prompt名称", paramType = "param"),
            @ApiImplicitParam(name = "belong", value = "prompt归属", required = true, paramType = "param"),
            @ApiImplicitParam(name = "types", value = "prompt类别,多个以逗号（,）分隔", paramType = "param")
    })
    public List<PromptVO> queryList(@RequestParam(name = "name", required = false) String name,
                                    @RequestParam(name = "belong") String belong,
                                    @RequestParam(name = "types", required = false) String types) {
        PromptVO prompt = new PromptVO();
        prompt.setName(name);
        prompt.setBelong(belong);
        prompt.setTypes(types);
        return promptService.queryList(prompt);
    }

    @ApiOperation(value = "查询prompt详情", notes = "查询prompt详情")
    @GetMapping("/detail")
    public PromptVO detail(@RequestParam("id") Long id) {
        return promptService.queryById(id);
    }

    @ApiOperation(value = "根据当前用户获取prompt数量", notes = "根据当前用户获取prompt数量")
    @GetMapping("/getPromptCountByUserId")
    public Long getPromptCountByUserId() {
        return promptService.getPromptCount();
    }


    @ApiOperation(value = "新增prompt", notes = "新增prompt")
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Prompt prompt) {
        if (Objects.isNull(prompt.getName())) {
            return CommonResponseUtil.responseMap(false, "prompt 名称不能为空！");
        }
        if (Objects.isNull(prompt.getPromptText())) {
            return CommonResponseUtil.responseMap(false, "prompt 提示词内容不能为空！");
        }
        promptService.insert(prompt);
        return CommonResponseUtil.responseMap(true, "新增成功！");
    }

    @ApiOperation(value = "prompt 内容识别变量标识符", notes = "prompt 内容识别变量标识符")
    @PostMapping("/getIdentifier")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "promptText", value = "prompt内容", paramType = "body")
    })
    public String getIdentifier(@ApiIgnore @RequestBody String promptText) {
        if (Objects.isNull(promptText)) {
            return null;
        }
        return promptService.getIdentifier(promptText);
    }

    @ApiOperation(value = "prompt 内容识别变量", notes = "prompt 内容识别变量")
    @PostMapping("/getVariable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "promptText", value = "prompt内容", paramType = "body")
    })
    public List<String> getVariable(@ApiIgnore @RequestBody String promptText) {
        if (Objects.isNull(promptText)) {
            return Collections.emptyList();
        }
        String identifier = promptService.getIdentifier(promptText);
        return promptService.getVariable(identifier, promptText);
    }

    @ApiOperation(value = "修改prompt", notes = "修改prompt")
    @PostMapping("/updateById")
    public Map<String, Object> update(@RequestBody Prompt prompt) {
        if (Objects.isNull(prompt.getId())) {
            return CommonResponseUtil.responseMap(false, "prompt 的ID不能为空！");
        }
        if (Objects.isNull(prompt.getName())) {
            return CommonResponseUtil.responseMap(false, "prompt 名称不能为空！");
        }
        if (Objects.isNull(prompt.getPromptText())) {
            return CommonResponseUtil.responseMap(false, "prompt 提示词内容不能为空！");
        }
        promptService.updateById(prompt);
        return CommonResponseUtil.responseMap(true, "修改成功！");
    }

    @ApiOperation(value = "删除prompt", notes = "删除prompt")
    @DeleteMapping("/deleteById")
    public Map<String, Object> deleteById(@RequestParam("id") Long id) {
        if (Objects.isNull(id)) {
            return CommonResponseUtil.responseMap(false, "prompt 的ID不能为空！");
        }
        promptService.deleteById(id);
        return CommonResponseUtil.responseMap(true, "删除成功！");
    }
}
