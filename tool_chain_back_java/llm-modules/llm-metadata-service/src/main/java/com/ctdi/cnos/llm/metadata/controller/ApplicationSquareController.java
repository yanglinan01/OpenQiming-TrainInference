/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.application.ApplicationSquare;
import com.ctdi.cnos.llm.beans.meta.application.ApplicationSquareVO;
import com.ctdi.cnos.llm.metadata.service.ApplicationSquareService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 应用广场 控制层
 *
 * @author huangjinhua
 * @since 2024/6/11
 */
@Api(tags = {"应用广场接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/applicationSquare")
public class ApplicationSquareController {

    private final ApplicationSquareService appService;

    @ApiOperation(value = "分页查询应用列表", notes = "分页查询应用列表")
    @GetMapping("/queryPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页大小，默认为20", paramType = "param"),
            @ApiImplicitParam(name = "currentPage", value = "当前页，默认为1", paramType = "param")
    })
    public Map<String, Object> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                         @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                         ApplicationSquareVO appVO) {
        Page<ApplicationSquareVO> page = new Page<>(currentPage, pageSize);
        page.addOrder(CollUtil.newLinkedList(OrderItem.asc("a.experience"), OrderItem.asc("a.create_date")));
        appService.queryPage(page, appVO);
        Map<String, Object> result = new HashMap<>(2);
        result.put("total", page.getTotal());
        result.put("rows", page.getRecords());
        return result;
    }

    @ApiOperation(value = "查询应用列表", notes = "查询应用列表")
    @GetMapping("/queryList")
    public List<ApplicationSquareVO> queryList(ApplicationSquareVO appVO) {
        return appService.queryList(appVO);
    }

    @ApiOperation(value = "查询应用详情", notes = "查询应用详情")
    @GetMapping("/queryById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "应用ID", paramType = "param"),
    })
    public ApplicationSquareVO queryById(@RequestParam("id") Long id) {
        return appService.queryById(id);
    }

    @ApiOperation(value = "新增应用", notes = "新增应用")
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody ApplicationSquare app) {
        if (Objects.isNull(app.getName())) {
            return CommonResponseUtil.responseMap(false, "应用名称不能为空！");
        }
        /*if (Objects.isNull(app.getType())) {
            return CommonResponseUtil.responseMap(false, "应用类型不能为空！");
        }*/
        appService.insert(app);
        return CommonResponseUtil.responseMap(true, "新增成功！");
    }


    @ApiOperation(value = "修改应用", notes = "修改应用")
    @PostMapping("/updateById")
    public Map<String, Object> updateById(@RequestBody ApplicationSquare app) {
        if (Objects.isNull(app.getId())) {
            return CommonResponseUtil.responseMap(false, "应用的ID不能为空！");
        }
        if (Objects.isNull(app.getName())) {
            return CommonResponseUtil.responseMap(false, "应用名称不能为空！");
        }
        /*if (Objects.isNull(app.getType())) {
            return CommonResponseUtil.responseMap(false, "应用类型不能为空！");
        }*/
        appService.updateById(app);
        return CommonResponseUtil.responseMap(true, "修改成功！");
    }

    @ApiOperation(value = "删除应用", notes = "删除应用")
    @DeleteMapping("/deleteById")
    public Map<String, Object> deleteById(@RequestParam("id") Long id) {
        if (Objects.isNull(id)) {
            return CommonResponseUtil.responseMap(false, "应用的ID不能为空！");
        }
        appService.deleteById(id);
        return CommonResponseUtil.responseMap(true, "删除成功！");
    }

}
