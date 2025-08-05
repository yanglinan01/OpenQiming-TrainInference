package com.ctdi.cnos.llm.controller.register;


import com.ctdi.cnos.llm.beans.register.DcoosApiTemplate;
import com.ctdi.cnos.llm.feign.register.DcoosApiTemplateServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author wangyb
 * @description
 * @data 2024/4/17 17:01
 */
@Slf4j
@RestController
@SuppressWarnings("all")
@Api(tags = {"DcoosApiTemplate接口"})
@RequestMapping(value = "/web/dcoosApiTemplate")
public class DcoosApiTemplateWebController {

    @Autowired
    DcoosApiTemplateServiceClientFeign dcoosApiTemplateServiceClientFeign;

    @GetMapping("/queryById/{id}")
    @ApiOperation(value = "查询DcoosApiTemplate详情")
    public OperateResult<DcoosApiTemplate> queryById(@PathVariable("id") BigDecimal id) {
        try {
            DcoosApiTemplate dcoosApiTemplate = dcoosApiTemplateServiceClientFeign.queryById(id);
            return new OperateResult<>(true, "", dcoosApiTemplate);
        } catch (Exception ex) {
            log.error("DcoosApiTemplate详情查询异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }


    @GetMapping("/queryByCode/{code}")
    @ApiOperation(value = "根据编码查询DcoosApiTemplate详情")
    public OperateResult<DcoosApiTemplate> queryByCode(@PathVariable("code") String code) {
        try {
            DcoosApiTemplate dcoosApiTemplate = dcoosApiTemplateServiceClientFeign.queryByCode(code);
            return new OperateResult<>(true, "", dcoosApiTemplate);
        } catch (Exception ex) {
            log.error("DcoosApiTemplate详情查询异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }

    @PostMapping("/queryList")
    @ApiOperation(value = "查询DcoosApiTemplate列表")
    public OperateResult<List<DcoosApiTemplate>> queryList(@RequestBody DcoosApiTemplate dcoosApiTemplate) {
        try {
            List<DcoosApiTemplate> list = dcoosApiTemplateServiceClientFeign.queryList(dcoosApiTemplate);
            return new OperateResult<>(true, "", list);
        } catch (Exception ex) {
            log.error("DcoosApiTemplate列表查询异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }


    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询DcoosApiTemplate")
    public OperateResult<Map<String, Object>> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                                        @RequestParam(name = "currentPage", required = false, defaultValue = "1")  long currentPage,
                                                        @RequestBody DcoosApiTemplate dcoosApiTemplate) {
        try {
            Map<String, Object> result = dcoosApiTemplateServiceClientFeign.queryPage(pageSize, currentPage, dcoosApiTemplate);
            return new OperateResult<>(true, "", result);
        } catch (Exception ex) {
            log.error("DcoosApiTemplate分页查询异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }


    @GetMapping("/deleteById/{id}")
    @ApiOperation(value = "删除DcoosApiTemplate")
    public OperateResult<Void> deleteById(@PathVariable("id") BigDecimal id) {
        try {
            dcoosApiTemplateServiceClientFeign.deleteById(id);
            return new OperateResult<>(true, "删除成功", null);
        } catch (Exception ex) {
            log.error("DcoosApiTemplate删除异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }


    @PostMapping("/insert")
    @ApiOperation(value = "新增DcoosApiTemplate")
    public OperateResult<Void> insert(@RequestBody DcoosApiTemplate dcoosApiTemplate) {
        try {
            dcoosApiTemplateServiceClientFeign.insert(dcoosApiTemplate);
            return new OperateResult<>(true, "新增成功", null);
        } catch (Exception ex) {
            log.error("DcoosApiTemplate新增异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }


    @PostMapping("/updateById")
    @ApiOperation(value = "修改DcoosApiTemplate")
    public OperateResult<Void> updateById(@RequestBody DcoosApiTemplate dcoosApiTemplate) {
        try {
            dcoosApiTemplateServiceClientFeign.updateById(dcoosApiTemplate);
            return new OperateResult<>(true, "修改成功", null);
        } catch (Exception ex) {
            log.error("DcoosApiTemplate修改异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }

}

