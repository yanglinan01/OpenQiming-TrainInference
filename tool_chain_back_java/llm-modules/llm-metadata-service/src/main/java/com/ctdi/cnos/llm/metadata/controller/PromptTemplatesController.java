package com.ctdi.cnos.llm.metadata.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptTemplates;
import com.ctdi.cnos.llm.metadata.service.PromptTemplatesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提示词模板表(promptTemplates)表控制层
 *
 * @author wangyb
 * @since 2024-04-02 15:09:11
 */

@Api(tags = {"PromptTemplates接口"})
@RestController
@RequestMapping(value = "/promptTemplates")
public class PromptTemplatesController {
    /**
     * 服务对象
     */
    @Autowired
    PromptTemplatesService promptTemplatesService;

    @GetMapping("/queryById/{id}")
    @ApiOperation(value = "查询PromptTemplates详情")
    public PromptTemplates queryById(@PathVariable("id") BigDecimal id) {
        return promptTemplatesService.queryById(id);
    }


    @PostMapping("/queryList")
    @ApiOperation(value = "查询PromptTemplates列表")
    public List<PromptTemplates> queryList(@RequestBody PromptTemplates promptTemplates) {
        return promptTemplatesService.queryList(promptTemplates);
    }


    @PostMapping("/queryPage")
    @ApiOperation(value="分页查询PromptTemplates")
    public Map<String, Object> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                         @RequestParam(name = "currentPage", required = false, defaultValue = "1")long currentPage,
                                         @RequestBody PromptTemplates promptTemplates) {
        Page<PromptTemplates> page = new Page<>(currentPage, pageSize);
        page = promptTemplatesService.queryList(page, promptTemplates);
        Map<String, Object> result = new HashMap<>(2);
        result.put("total", page.getTotal());
        result.put("rows", page.getRecords());
        return result;
    }


    @GetMapping("/deleteById/{id}")
    @ApiOperation(value = "删除PromptTemplates")
    public Map<String, Object> deleteById(@PathVariable("id") BigDecimal id) {
        promptTemplatesService.deleteById(id);
        Map<String, Object> result = new HashMap<>(2);
        result.put("success", true);
        result.put("message", "删除成功");
        return result;
    }


    @PostMapping("/insert")
    @ApiOperation(value = "新增PromptTemplates")
    public Map<String, Object> insert(@RequestBody PromptTemplates promptTemplates) {
        promptTemplatesService.insert(promptTemplates);
        Map<String, Object> result = new HashMap<>(2);
        result.put("success", true);
        result.put("message", "新增成功");
        return result;
    }


    @PostMapping("/updateById")
    @ApiOperation(value = "修改PromptTemplates")
    public Map<String, Object> updateById(@RequestBody PromptTemplates promptTemplates) {
        promptTemplatesService.updateById(promptTemplates);
        Map<String, Object> result = new HashMap<>(2);
        result.put("success", true);
        result.put("message", "修改成功");
        return result;
    }


}

