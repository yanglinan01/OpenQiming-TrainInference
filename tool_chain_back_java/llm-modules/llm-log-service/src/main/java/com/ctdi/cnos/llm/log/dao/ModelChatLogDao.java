package com.ctdi.cnos.llm.log.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.log.chat.HourlyCallStats;
import com.ctdi.cnos.llm.beans.log.chat.HourlyTokenStats;
import com.ctdi.cnos.llm.beans.log.chat.ModelCallSummary;
import com.ctdi.cnos.llm.beans.log.chat.ModelChatLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 模型体验对话日志 数据操作访问接口。
 *
 * @author laiqi
 * @since 2024/07/16
 */
@InterceptorIgnore(dataPermission = "false") // 启用数据权限拦截
@Mapper
public interface ModelChatLogDao extends BaseDaoMapper<ModelChatLog> {

    List<ModelChatLog> queryExpiredDeployTask(@Param("offsetDay") Integer offsetDay);

    /**
     * 根据模型ID列表查询最新体验时间
     *
     * @param modelIdList 模型ID列表
     * @return 日志列表（模型ID，最新体验时间）
     */
    List<ModelChatLog> queryLastSendTimeByModelId(@Param("modelIdList") List<Long> modelIdList);

    List<HourlyTokenStats> getHourlyTokenStats(@Param("modelId") Long modelId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<HourlyCallStats> getHourlyCallStats(@Param("modelId") Long modelId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    ModelCallSummary getModelCallSummary(@Param("modelId") Long modelId);
}