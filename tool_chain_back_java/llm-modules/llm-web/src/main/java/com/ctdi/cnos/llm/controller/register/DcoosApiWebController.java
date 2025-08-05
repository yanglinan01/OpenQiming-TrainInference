package com.ctdi.cnos.llm.controller.register;

import com.ctdi.cnos.llm.beans.register.DcoosApi;
import com.ctdi.cnos.llm.beans.register.req.DcoosApiQueryReq;
import com.ctdi.cnos.llm.beans.register.req.DcoosApiReq;
import com.ctdi.cnos.llm.feign.register.DcoosApiServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zhangxue
 * @description
 * @data 2024/4/17 17:01
 */
@Slf4j
@RestController
@SuppressWarnings("all")
@Api(tags = {"DcoosApi接口"})
@RequestMapping(value = "/web/dcoosApi")
public class DcoosApiWebController {

    @Autowired
    private DcoosApiServiceClientFeign dcoosApiServiceClientFeign;

    @PostMapping("/addApi")
    public OperateResult addApi(@RequestBody DcoosApiReq req) {
        try {
            dcoosApiServiceClientFeign.addApi(req);
            return new OperateResult(true, "新增成功");
        } catch (Exception e) {
            log.error("新增异常", e);
            return new OperateResult(false, e.getMessage(), null);
        }

    }

    @PostMapping("/updateApi")
    public OperateResult updateApi(@RequestBody DcoosApiReq req) {
        if (req.getId() == null) {
            return new OperateResult(false, "id不能为空");
        }
        try {
            dcoosApiServiceClientFeign.updateApi(req);
            return new OperateResult(true, "修改成功");
        } catch (Exception e) {
            log.error("修改异常", e);
            return new OperateResult(false, e.getMessage(), null);
        }
    }

    @GetMapping("/queryById/{id}")
    public OperateResult queryById(@PathVariable("id") String id) {
        try {
            DcoosApi mmDcoosApi = dcoosApiServiceClientFeign.queryById(id);
            return new OperateResult(true, "查询成功", mmDcoosApi);
        } catch (Exception e) {
            log.error("查询异常", e);
            return new OperateResult(false, e.getMessage(), null);
        }
    }

    @PostMapping("/queryList")
    public OperateResult queryList(@RequestBody DcoosApiQueryReq req) {
        try {
            Map<String, Object> result = dcoosApiServiceClientFeign.queryList(req);
            return new OperateResult(true, "查询成功", result);
        } catch (Exception e) {
            log.error("查询异常", e);
            return new OperateResult(false, e.getMessage(), null);
        }

    }


    @PostMapping("/buildDcoosApi")
    @ApiOperation("构建DcoosApi文件")
    public OperateResult<String> buildDcoosApi(@RequestBody DcoosApi dcoosApi) {
        try {
            String result = dcoosApiServiceClientFeign.buildDcoosApi(dcoosApi);
            return new OperateResult<>(true, "", result);
        } catch (Exception e) {
            log.error("DCOSS_API注册文档构建异常", e);
            return new OperateResult<>(false, e.getMessage(), null);
        }

    }

}

