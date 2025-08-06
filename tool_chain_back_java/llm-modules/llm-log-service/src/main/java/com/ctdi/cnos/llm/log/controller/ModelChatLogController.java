package com.ctdi.cnos.llm.log.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.*;
import com.ctdi.cnos.llm.beans.log.chat.*;
import com.ctdi.cnos.llm.log.service.ModelChatLogService;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.ModelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模型体验对话日志 操作控制器类。
 *
 * @author laiqi
 * @since 2024/07/16
 */
@Api(tags = "模型体验对话日志接口", value = "ModelChatLogController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/modelChatLog")
public class ModelChatLogController {

    private final ModelChatLogService service;


    /**
     * 查询模型调用汇总信息。
     *
     * @param modelId 模型ID。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "查询模型调用汇总信息(指定模型)", notes = "查询模型调用汇总信息(指定模型)")
    @GetMapping(value = "/getModelCallSummary")
    public OperateResult<ModelCallSummary> getModelCallSummary(@RequestParam("modelId") Long modelId) {
        return OperateResult.success(service.getModelCallSummary(modelId));
    }

    /**
     * 按小时统计的Token数量(指定模型和时间范围)。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "按小时统计的Token数量(指定模型和时间范围)", notes = "按小时统计的Token数量(指定模型和时间范围)")
    @PostMapping(value = "/getHourlyTokenStats")
    public OperateResult<List<HourlyTokenStats>> getHourlyTokenStats(@Validated @RequestBody QueryHourlyStatsDTO queryParam) {
        return OperateResult.success(service.getHourlyTokenStats(queryParam));
    }

    /**
     * 按小时统计的调用数量(指定模型和时间范围)。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "按小时统计的调用数量(指定模型和时间范围)。", notes = "按小时统计的调用数量(指定模型和时间范围)。")
    @PostMapping(value = "/getHourlyCallStats")
    public OperateResult<List<HourlyCallStats>> getHourlyCallStats(@Validated @RequestBody QueryHourlyStatsDTO queryParam) {
        return OperateResult.success(service.getHourlyCallStats(queryParam));
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
        queryParam.setSortingFields(CollUtil.newArrayList(SortingField.desc(ModelChatLog.Fields.sendTime)));
        queryParam.ignoreAuth();
        return OperateResult.success(service.queryPage(queryParam));
    }

    /**
     * 分页列出符合过滤条件的模型体验对话日志列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取分页查询的模型体验对话日志列表信息", notes = "获取分页查询的模型体验对话日志信息")
    @PostMapping(value = "/queryPage")
    public PageResult<ModelChatLogVO> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return service.queryPage(queryParam);
    }

    /**
     * 列出符合过滤条件的模型体验对话日志列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取查询的模型体验对话日志列表信息", notes = "获取查询的模型体验对话日志信息")
    @PostMapping(value = "/queryList")
    public List<ModelChatLogVO> queryList(@RequestBody QueryParam queryParam) {
        return service.queryList(queryParam);
    }

    /**
     * 获取用户指定模型的最后10条模型体验对话日志列表。
     *
     * @param modelChatId 模型体验的ID。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取用户指定模型的最后10条模型体验对话日志列表", notes = "获取用户指定模型的最后10条模型体验对话日志列表")
    @GetMapping(value = "/queryUserLastTenLogs")
    public List<ModelChatLogVO> queryUserLastTenLogs(@ApiParam(value = "模型体验的ID", required = true, example = "1")
                                                     @NotNull(message = "模型体验的ID不能为空") @RequestParam("modelChatId") Long modelChatId) {
        return service.queryUserLastTenLogs(modelChatId);
    }

    /**
     * 查看模型体验对话日志对象详情。
     *
     * @param id 指定模型体验对话日志主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "根据ID获取模型体验对话日志详情", notes = "通过模型体验对话日志ID获取具体的模型体验对话日志信息")
    @ApiOperationSupport(ignoreParameters = {"pageParam", "dataPermRuleType"})
    @GetMapping(value = "/queryById")
    public ModelChatLogVO queryById(@ApiParam(value = "模型体验对话日志ID", required = true, example = "1")
                                    @NotNull(message = "模型体验对话日志ID不能为空") @RequestParam("id") Long id) {
        return service.queryById(id, true);
    }

    /**
     * 添加模型体验对话日志操作。
     *
     * @param modelChatLogDto 添加模型体验对话日志对象。
     * @return 应答结果对象。
     */
    @AuthIgnore
    @ApiOperation(value = "创建模型体验对话日志", notes = "根据请求体中的模型体验对话日志信息创建")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody ModelChatLogDTO modelChatLogDto) {
        ModelChatLog modelChatLog = ModelUtil.copyTo(modelChatLogDto, ModelChatLog.class);
        return service.save(modelChatLog) ? OperateResult.success(ApplicationConstant.SAVE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
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
        ModelChatLog modelChatLog = ModelUtil.copyTo(modelChatLogDto, ModelChatLog.class);
        return service.updateById(modelChatLog) ? OperateResult.success(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 删除指定的模型体验对话日志。
     *
     * @param id 指定模型体验对话日志主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "删除模型体验对话日志", notes = "根据ID删除指定的模型体验对话日志")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> deleteById(@ApiParam(value = "模型体验对话日志ID", required = true, example = "1")
                                            @NotNull(message = "模型体验对话日志ID不能为空") @RequestParam("id") Long id) {
        return service.deleteById(id) ? OperateResult.success(ApplicationConstant.DELETE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
    }


    /**
     * 删除模型体验对话日志过期记录。
     *
     * @param offsetDay 删除模型对话历史记录过期时间,-15表示前15天的需要清除。
     * @return 删除记录数量。
     */
    @AuthIgnore
    @ApiOperation(value = "删除模型体验对话日志过期记录", notes = "删除指定天数前的模型体验对话日志记录")
    @GetMapping(value = "/deleteExpiredLog")
    public OperateResult<Long> deleteExpiredLog(@ApiParam(value = "指定偏移天数。", required = true, example = "-15")
                                                @Max(value = -1, message = "指定偏移天数最大值为 -1") @RequestParam(value = "offsetDay", defaultValue = "-15") Integer offsetDay) {
        return OperateResult.success(service.deleteExpiredLog(offsetDay));
    }


    @AuthIgnore
    @ApiOperation(value = "查询超时的部署模型", notes = "查询超时的部署模型")
    @GetMapping(value = "/queryExpiredDeployTask")
    public List<ModelChatLog> queryExpiredDeployTask(@RequestParam(value = "offsetDay", defaultValue = "1") Integer offsetDay) {
        return service.queryExpiredDeployTask(offsetDay);
    }

    @AuthIgnore
    @ApiOperation(value = "查询x天前的模型对话", notes = "查询x天前的模型对话")
    @PostMapping(value = "/queryLastSendTimeByModelId")
    public List<ModelChatLog> queryLastSendTimeByModelId(@RequestBody ModelChatLogVO modelChatLogVO) {
        List<Long> modelIdList = modelChatLogVO.getModelIdList();
        List<ModelChatLog> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(modelIdList)) {
            Integer offsetDay = modelChatLogVO.getOffsetDay();
            if (offsetDay == null) {
                offsetDay = 1;
            }
            Date compareDate = DateUtil.offsetDay(DateUtil.date(), -offsetDay);
            List<ModelChatLog> modelChatLogs = service.queryLastSendTimeByModelId(modelIdList);
            List<Long> modelChatModelId = new ArrayList<>();
            if (CollUtil.isNotEmpty(modelChatLogs)) {
                modelChatModelId = modelChatLogs.stream().map(ModelChatLog::getModelChatId).collect(Collectors.toList());
                modelChatLogs.forEach(log -> {
                    Date sendTime = log.getSendTime();
                    if (sendTime != null && DateUtil.compare(sendTime, compareDate) < 0) {
                        list.add(log);
                    }
                });
            }

            //找不到对应的模型对话，表明定时任务已经做了清除 -默认添加15天的时间
            if (CollUtil.isNotEmpty(modelChatModelId)) {
                //获取两个集合的差集，表明问答已经超期
                List<Long> disjunction = (List<Long>) CollUtil.disjunction(modelIdList, modelChatModelId);
                if (CollUtil.isNotEmpty(modelChatModelId)) {
                    disjunction.forEach(modelId -> {
                        ModelChatLog modelChatLog = new ModelChatLog();
                        modelChatLog.setModelChatId(modelId);
                        //模拟30天前
                        modelChatLog.setSendTime(DateUtil.offsetDay(DateUtil.date(), -30));
                        list.add(modelChatLog);
                    });
                }

            }
        }
        return list;
    }

}