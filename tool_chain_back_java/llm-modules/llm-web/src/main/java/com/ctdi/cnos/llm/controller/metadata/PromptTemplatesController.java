package com.ctdi.cnos.llm.controller.metadata;

import com.ctdi.cnos.llm.beans.meta.prompt.PromptTemplates;
import com.ctdi.cnos.llm.feign.metadata.PromptTemplatesServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
@Slf4j
public class PromptTemplatesController {
    /**
     * 服务对象
     */
    @Autowired
    PromptTemplatesServiceClientFeign promptTemplatesServiceClientFeign;

    @GetMapping("/queryById/{id}")
    @ApiOperation(value = "查询PromptTemplates详情")
    public OperateResult<PromptTemplates> queryById(@PathVariable("id") BigDecimal id) {
        try {
            PromptTemplates promptTemplates = promptTemplatesServiceClientFeign.queryById(id);
            return new OperateResult<>(true, "", promptTemplates);
        } catch (Exception e) {
            log.error("PromptTemplates详情查询异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @PostMapping("/queryList")
    @ApiOperation(value = "查询PromptTemplates列表")
    public OperateResult<List<PromptTemplates>> queryList(@RequestBody PromptTemplates promptTemplates) {
        try {
            List<PromptTemplates> list = promptTemplatesServiceClientFeign.queryList(promptTemplates);
            return new OperateResult<>(true, "", list);
        } catch (Exception e) {
            log.error("PromptTemplates列表查询异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询PromptTemplates")
    public OperateResult<Map<String, Object>> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                                       @RequestParam(name = "currentPage", required = false, defaultValue = "1")  long currentPage,
                                                       @RequestBody PromptTemplates promptTemplates) {
        try {
            Map<String, Object> result = promptTemplatesServiceClientFeign.queryPage(pageSize, currentPage, promptTemplates);
            return new OperateResult<>(true, "", result);
        } catch (Exception e) {
            log.error("PromptTemplates分页查询异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @GetMapping("/deleteById/{id}")
    @ApiOperation(value = "删除PromptTemplates")
    public OperateResult<Void> deleteById(@PathVariable("id") BigDecimal id) {
        try {
            promptTemplatesServiceClientFeign.deleteById(id);
            return new OperateResult<>(true, "删除成功", null);
        } catch (Exception e) {
            log.error("PromptTemplates删除异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @PostMapping("/insert")
    @ApiOperation(value = "新增PromptTemplates")
    public OperateResult<Void> insert(@RequestBody PromptTemplates promptTemplates) {
        try {
            promptTemplatesServiceClientFeign.insert(promptTemplates);
            return new OperateResult<>(true, "新增成功", null);
        } catch (Exception e) {
            log.error("PromptTemplates新增异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @PostMapping("/updateById")
    @ApiOperation(value = "修改PromptTemplates")
    public OperateResult<Void> updateById(@RequestBody PromptTemplates promptTemplates) {
        try {
            promptTemplatesServiceClientFeign.updateById(promptTemplates);
            return new OperateResult<>(true, "修改成功", null);
        } catch (Exception e) {
            log.error("PromptTemplates修改异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


}

