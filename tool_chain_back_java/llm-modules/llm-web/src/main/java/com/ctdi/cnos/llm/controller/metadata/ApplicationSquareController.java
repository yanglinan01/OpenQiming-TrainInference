/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.controller.metadata;

import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.beans.meta.application.ApplicationSquareVO;
import com.ctdi.cnos.llm.feign.metadata.ApplicationSquareServiceClientFeign;
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
import java.util.Map;

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
@Slf4j
public class ApplicationSquareController {

    private final ApplicationSquareServiceClientFeign appClient;

    @ApiOperation(value = "分页查询应用列表", notes = "分页查询应用列表")
    @GetMapping("/queryPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页大小，默认为20", paramType = "param"),
            @ApiImplicitParam(name = "currentPage", value = "当前页，默认为1", paramType = "param")
    })
    public OperateResult<Map<String, Object>> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                                        @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                                        ApplicationSquareVO appVO) {
        try {
            Map<String, Object> result = appClient.queryPage(pageSize, currentPage, appVO);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("分页查询应用列表异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "查询应用列表", notes = "查询应用列表")
    @GetMapping("/queryList")
    public OperateResult<List<ApplicationSquareVO>> queryList(ApplicationSquareVO app) {
        try {
            List<ApplicationSquareVO> result = appClient.queryList(app);
            if (result != null && !result.isEmpty()) {
                result.get(0).setIsActivate(SystemConstant.YES);
            }
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("查询应用列表异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "查询应用详情", notes = "查询应用详情")
    @GetMapping("/queryById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "应用ID", paramType = "param"),
    })
    public OperateResult<ApplicationSquareVO> queryById(@RequestParam("id") Long id) {
        try {
            ApplicationSquareVO result = appClient.queryById(id);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("查询应用详情异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }
}