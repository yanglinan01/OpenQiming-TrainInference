package com.ctdi.cnos.llm.feign.log;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.log.chat.*;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 模型体验对话日志服务远程数据操作访问接口。
 *
 * @author laiqi
 * @since 2024/07/16
 */
@Component
@FeignClient(value = RemoteConstont.LOG_SERVICE_NAME, path = "${cnos.server.llm-log-service.contextPath}")
public interface ModelChatLogServiceClientFeign {

    /**
     * 分页列出符合过滤条件的模型体验对话日志列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping(value = "/modelChatLog/queryPage")
    PageResult<ModelChatLogVO> queryPage(@RequestBody QueryParam queryParam);

    /**
     * 列出符合过滤条件的模型体验对话日志列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping(value = "/modelChatLog/queryList")
    List<ModelChatLogVO> queryList(@RequestBody QueryParam queryParam);

    /**
     * 查看模型体验对话日志对象详情。
     *
     * @param id 指定模型体验对话日志主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/modelChatLog/queryById")
    ModelChatLogVO queryById(@RequestParam("id") Long id);

    /**
     * 添加模型体验对话日志操作。
     *
     * @param modelChatLogDto 添加模型体验对话日志对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/modelChatLog/add")
    OperateResult<String> add(@RequestBody ModelChatLogDTO modelChatLogDto);

    /**
     * 更新模型体验对话日志操作。
     *
     * @param modelChatLogDto 更新模型体验对话日志对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/modelChatLog/updateById")
    OperateResult<String> updateById(@RequestBody ModelChatLogDTO modelChatLogDto);

    /**
     * 删除指定的模型体验对话日志。
     *
     * @param id 指定模型体验对话日志主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/modelChatLog/deleteById")
    OperateResult<String> deleteById(@RequestParam("id") Long id);

    /**
     * 删除模型体验对话日志过期记录。
     *
     * @param offsetDay 删除模型对话历史记录过期时间,-15表示前15天的需要清除。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/modelChatLog/deleteExpiredLog")
    OperateResult<Long> deleteExpiredLog(@RequestParam("offsetDay") Integer offsetDay);

    /**
     * 查询超时的部署模型
     *
     * @param offsetDay 过期时间(单位:day)
     * @return 过期模型列表
     */
    @GetMapping(value = "/modelChatLog/queryExpiredDeployTask")
    List<ModelChatLog> queryExpiredDeployTask(@RequestParam(value = "offsetDay", defaultValue = "1") Integer offsetDay);

    /**
     * 查询x天前的模型对话
     *
     * @param modelChatLogVO 模型对象（模型ID列表+ X天数）
     * @return 过期模型列表(模型ID + 最近对话时间)
     */
    @PostMapping(value = "/modelChatLog/queryLastSendTimeByModelId")
    List<ModelChatLog> queryLastSendTimeByModelId(@RequestBody ModelChatLogVO modelChatLogVO);

    /**
     * 获取用户指定模型的最后10条模型体验对话日志列表。
     *
     * @param modelChatId 模型体验的ID。
     * @return 应答结果对象，包含查询结果集。
     */
    @GetMapping(value = "/modelChatLog/queryUserLastTenLogs")
    List<ModelChatLogVO> queryUserLastTenLogs(@RequestParam("modelChatId") Long modelChatId);

    /**
     * 查询模型调用汇总信息。
     *
     * @param modelId 模型ID。
     * @return 应答结果对象，包含查询结果集。
     */
    @GetMapping(value = "/modelChatLog/getModelCallSummary")
    OperateResult<ModelCallSummary> getModelCallSummary(@RequestParam("modelId") Long modelId);

    /**
     * 按小时统计的Token数量(指定模型和时间范围)。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping(value = "/modelChatLog/getHourlyTokenStats")
    OperateResult<List<HourlyTokenStats>> getHourlyTokenStats(@Validated @RequestBody QueryHourlyStatsDTO queryParam);

    /**
     * 按小时统计的调用数量(指定模型和时间范围)。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping(value = "/modelChatLog/getHourlyCallStats")
    OperateResult<List<HourlyCallStats>> getHourlyCallStats(@Validated @RequestBody QueryHourlyStatsDTO queryParam);

    /**
     * 根据模型和时间范围获取对话日志(指定模型和时间范围)
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     *
     */
    @PostMapping(value = "/modelChatLog/queryModelChatLog")
    OperateResult<PageResult<ModelChatLogVO>> queryModelChatLog(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam);

}