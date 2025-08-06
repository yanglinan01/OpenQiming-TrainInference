package com.ctdi.cnos.llm.log.service;

import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.log.chat.*;

import java.util.List;

/**
 * 模型体验对话日志 数据操作服务接口。
 *
 * @author laiqi
 * @since 2024/07/16
 */
public interface ModelChatLogService extends IBaseService<ModelChatLog, ModelChatLogVO> {

    /**
     * 删除模型体验对话日志过期记录
     *
     * @param offsetDay 指定偏移天数。负数。
     * @return 删除记录数
     */
    long deleteExpiredLog(Integer offsetDay);


    /**
     * 根据模型ID列表查询最新体验时间
     *
     * @param modelIdList 模型ID列表
     * @return 日志列表（模型ID，最新体验时间）
     */
    List<ModelChatLog> queryLastSendTimeByModelId(List<Long> modelIdList);

    /**
     * 根据对话日志查询过期的模型
     *
     * @param offsetDay 指定过期时间
     * @return 过期模型列表
     */
    List<ModelChatLog> queryExpiredDeployTask(Integer offsetDay);

    /**
     * 查询用户指定模型的最后10条模型体验对话日志列表
     *
     * @param modelChatId 模型ID
     * @return 数据列表
     */
    List<ModelChatLogVO> queryUserLastTenLogs(Long modelChatId);

    /**
     * 按小时统计的Token数量(指定模型和时间范围)
     * @param queryParam
     * @return
     */
    List<HourlyTokenStats> getHourlyTokenStats(QueryHourlyStatsDTO queryParam);

    /**
     * 按小时统计的调用数量(指定模型和时间范围)
     * @param queryParam
     * @return
     */
    List<HourlyCallStats> getHourlyCallStats(QueryHourlyStatsDTO queryParam);

    /**
     * 获取模型调用汇总
     * @param modelId 模型ID
     * @return
     */
    ModelCallSummary getModelCallSummary(Long modelId);
}