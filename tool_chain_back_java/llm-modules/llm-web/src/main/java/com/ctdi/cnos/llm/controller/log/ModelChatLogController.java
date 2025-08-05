package com.ctdi.cnos.llm.controller.log;

import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.log.chat.*;
import com.ctdi.cnos.llm.feign.log.ModelChatLogServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

;


/**
 * 模型体验对话日志服务远程数据操作访问接口。
 *
 * @author laiqi
 * @since 2024/07/16
 */
@Api(tags = "模型体验对话日志接口", value = "ModelChatLogController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/modelChatLog")
public class ModelChatLogController {

	private final ModelChatLogServiceClientFeign serviceClient;

    /**
     * 查询模型调用汇总信息。
     *
     * @param modelId 模型ID。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "查询模型调用汇总信息(指定模型)", notes = "查询模型调用汇总信息(指定模型)")
    @GetMapping(value = "/getModelCallSummary")
    public OperateResult<ModelCallSummary> getModelCallSummary(@RequestParam("modelId") Long modelId) {
        return serviceClient.getModelCallSummary(modelId);
    }

    /**
     * 按小时统计的Token数量(指定模型和时间范围)。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "按小时统计的Token数量(指定模型和时间范围)", notes = "按小时统计的Token数量(指定模型和时间范围)")
    @PostMapping(value = "/getHourlyTokenStats")
    public OperateResult<JSONObject> getHourlyTokenStats(@Validated @RequestBody QueryHourlyStatsDTO queryParam) {
        OperateResult<List<HourlyTokenStats>> operateResult = serviceClient.getHourlyTokenStats(queryParam);
        if (operateResult.isSuccess()) {
            JSONObject result = new JSONObject();
            List<HourlyTokenStats> data = operateResult.getData();
            result.put("xAxisData", data.stream().map(HourlyTokenStats::getHour).collect(Collectors.toList()));
            result.put(HourlyTokenStats.Fields.promptTokensSum, data.stream().map(HourlyTokenStats::getPromptTokensSum).collect(Collectors.toList()));
            result.put(HourlyTokenStats.Fields.completionTokensSum, data.stream().map(HourlyTokenStats::getCompletionTokensSum).collect(Collectors.toList()));
            result.put(HourlyTokenStats.Fields.totalTokensSum, data.stream().map(HourlyTokenStats::getTotalTokensSum).collect(Collectors.toList()));
            return OperateResult.success(result);
        }
        return OperateResult.error(operateResult);
    }

    /**
     * 按小时统计的调用数量(指定模型和时间范围)。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "按小时统计的调用数量(指定模型和时间范围)。", notes = "按小时统计的调用数量(指定模型和时间范围)。")
    @PostMapping(value = "/getHourlyCallStats")
    public OperateResult<JSONObject> getHourlyCallStats(@Validated @RequestBody QueryHourlyStatsDTO queryParam) {
        OperateResult<List<HourlyCallStats>> operateResult = serviceClient.getHourlyCallStats(queryParam);
        if (operateResult.isSuccess()) {
            JSONObject result = new JSONObject();
            List<HourlyCallStats> data = operateResult.getData();
            result.put("xAxisData", data.stream().map(HourlyCallStats::getHour).collect(Collectors.toList()));
            result.put(HourlyCallStats.Fields.totalCalls, data.stream().map(HourlyCallStats::getTotalCalls).collect(Collectors.toList()));
            return OperateResult.success(result);
        }
        return OperateResult.error(operateResult);
    }

    /**
     * 根据模型和时间范围获取对话日志(指定模型和时间范围)
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     *
     * <blockquote><pre>
     *     {
     *   "filterMap": {
     *     "modelChatId": 1839191337673703424,
     *     "startTime": "2024-11-25 10:00:00",
     *     "endTime": "2024-11-26 14:40:00"
     *   },
     *   "pageParam": {
     *     "pageNum": 1,
     *     "pageSize": 10
     *   },
     *   "withDict": true
     * }
     * </pre></blockquote>
     *
     */
    @ApiOperation(value = "根据模型和时间范围获取对话日志(指定模型和时间范围)", notes = "根据模型和时间范围获取对话日志(指定模型和时间范围)")
    @PostMapping(value = "/queryModelChatLog")
    public OperateResult<PageResult<ModelChatLogVO>> queryModelChatLog(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return this.serviceClient.queryModelChatLog(queryParam);
    }


    /**
     * 获取用户指定模型的最后10条模型体验对话日志列表。
     *
     * @param modelChatId 模型体验的ID。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取用户指定模型的最后10条模型体验对话日志列表", notes = "获取用户指定模型的最后10条模型体验对话日志列表")
    @GetMapping(value = "/queryUserLastTenLogs")
    public OperateResult<List<ModelChatLogVO>> queryLastTenLogs(@ApiParam(value = "模型体验的ID", required = true, example = "1")
                                                 @NotNull(message = "模型体验的ID不能为空") @RequestParam("modelChatId") Long modelChatId) {
        List<ModelChatLogVO> results = this.serviceClient.queryUserLastTenLogs(modelChatId);
        return OperateResult.success(results);
    }

    /**
     * 分页列出符合过滤条件的模型体验对话日志列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
	@ApiOperation(value = "获取分页查询的模型体验对话日志列表信息", notes = "获取分页查询的模型体验对话日志信息")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<ModelChatLogVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
		PageResult<ModelChatLogVO> data = this.serviceClient.queryPage(queryParam);
        return OperateResult.success(data);
    }

    /**
     * 列出符合过滤条件的模型体验对话日志列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
	@ApiOperation(value = "获取查询的模型体验对话日志列表信息", notes = "获取查询的模型体验对话日志信息")
    @PostMapping(value = "/queryList")
    public OperateResult<List<ModelChatLogVO>> queryList(@RequestBody QueryParam queryParam) {
		List<ModelChatLogVO> data = this.serviceClient.queryList(queryParam);
        return OperateResult.success(data);
    }	

    /**
     * 查看模型体验对话日志对象详情。
     *
     * @param id 指定模型体验对话日志主键Id。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "根据ID获取模型体验对话日志详情", notes = "通过模型体验对话日志ID获取具体的模型体验对话日志信息")
	@GetMapping(value = "/queryById")
    public OperateResult<ModelChatLogVO> queryById(@ApiParam(value = "模型体验对话日志ID", required = true, example = "1")
                          @NotNull(message = "模型体验对话日志ID不能为空") @RequestParam("id") Long id) {
		ModelChatLogVO data = this.serviceClient.queryById(id);
        return OperateResult.success(data);
    }

    /**
     * 添加模型体验对话日志操作。
     *
     * @param modelChatLogDto 添加模型体验对话日志对象。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "创建模型体验对话日志", notes = "根据请求体中的模型体验对话日志信息创建")
	@ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody ModelChatLogDTO modelChatLogDto) {
		return this.serviceClient.add(modelChatLogDto);
    }
	
    /**
     * 更新模型体验对话日志操作。
     *
     * @param modelChatLogDto 更新模型体验对话日志对象。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "更新模型体验对话日志", notes = "根据ID更新指定的模型体验对话日志信息")
    @PostMapping(value = "/updateById")
    public OperateResult<String> updateById(@Validated(Groups.UPDATE.class) @RequestBody ModelChatLogDTO modelChatLogDto) {
		return this.serviceClient.updateById(modelChatLogDto);
    }

    /**
     * 删除指定的模型体验对话日志。
     *
     * @param id 指定模型体验对话日志主键Id。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "删除模型体验对话日志", notes = "根据ID删除指定的模型体验对话日志")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> delete(@ApiParam(value = "模型体验对话日志ID", required = true, example = "1")
                          @NotNull(message = "模型体验对话日志ID不能为空") @RequestParam("id") Long id) {
        return this.serviceClient.deleteById(id);
    }

}