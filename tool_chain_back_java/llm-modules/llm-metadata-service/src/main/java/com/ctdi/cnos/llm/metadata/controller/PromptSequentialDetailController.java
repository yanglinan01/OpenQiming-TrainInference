package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetail;
import com.ctdi.cnos.llm.metadata.service.PromptSequentialDetailService;
import com.ctdi.cnos.llm.util.CommonResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 时序测试数据集详情 操作控制器类。
 *
 * @author laiqi
 * @since 2024/08/16
 */
@Api(tags = "时序测试数据集详情接口", value = "PromptSequentialDetailController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/promptSequentialDetail")
public class PromptSequentialDetailController {
	
    private final PromptSequentialDetailService service;

    /**
     * 分页列出符合过滤条件的时序测试数据集详情列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取分页查询的时序测试数据集详情列表信息", notes = "获取分页查询的时序测试数据集详情信息")
    @PostMapping(value = "/queryPage")
    public PageResult<PromptSequentialDetail> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return service.queryPage(queryParam);
    }
	

    /**
     * 列出符合过滤条件的时序测试数据集详情列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
	@ApiOperation(value = "获取查询的时序测试数据集详情列表信息", notes = "获取查询的时序测试数据集详情信息")
    @PostMapping(value = "/queryList")
    public List<PromptSequentialDetail> queryList(@RequestBody QueryParam queryParam) {
        return service.queryList(queryParam);
    }

    /**
     * 根据数据集id统计对应的电路数量。
     *
     * @param dataSetId 数据集id。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "根据数据集id统计对应的电路数量", notes = "根据数据集id统计对应的电路数量")
    @GetMapping(value = "/countCircuitByDataSetId")
    public Long countCircuitByDataSetId(@RequestParam("dataSetId") Long dataSetId) {
        return service.countCircuitByDataSetId(dataSetId);
    }

    /**
     * 时序测试数据集批量新增
     * @param paramList
     * @return
     */
    @ApiOperation(value = "批量时序测试数据集新增", notes = "批量时序测试数据集新增")
    @PostMapping("/addBatch")
    public Map<String, Object> addBatch(@RequestBody List<PromptSequentialDetail> paramList) {
        service.insertBatch(paramList ,null);
        return CommonResponseUtil.responseMap(true, "批量新增时序测试数据集成功！");
    }

}