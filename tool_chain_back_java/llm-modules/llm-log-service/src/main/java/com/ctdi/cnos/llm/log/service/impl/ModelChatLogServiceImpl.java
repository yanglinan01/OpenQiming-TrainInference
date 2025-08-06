package com.ctdi.cnos.llm.log.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import com.ctdi.cnos.llm.base.object.*;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.log.chat.*;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.log.dao.ModelChatLogDao;
import com.ctdi.cnos.llm.log.service.ModelChatLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 模型体验对话日志 数据操作服务类
 *
 * @author laiqi
 * @since 2024/07/16
 */
@RequiredArgsConstructor
@Service("modelChatLogService")
public class ModelChatLogServiceImpl extends BaseService<ModelChatLogDao, ModelChatLog, ModelChatLogVO> implements ModelChatLogService {

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<ModelChatLog> wrapper, QueryParam queryParam) {
        ModelChatLog filter = queryParam.getFilterDto(ModelChatLog.class);
        wrapper.eqIfPresent(ModelChatLog::getModelChatId, filter.getModelChatId());
        if (BooleanUtil.isFalse(Convert.toBool(queryParam.getFilterMap().get(QueryParam.IGNORE_AUTH), false))) {
            wrapper.eqIfPresent(ModelChatLog::getSendUserId, UserContextHolder.getUserId());
        }
        wrapper.isNotNull(ModelChatLog::getSendMessage);
        wrapper.betweenIfPresent(ModelChatLog::getSendTime, filter.getStartTime(), filter.getEndTime());
    }

    @Override
    public long deleteExpiredLog(Integer offsetDay) {
        LambdaQueryWrapperX<ModelChatLog> queryWrapper = new LambdaQueryWrapperX<ModelChatLog>()
                .leIfPresent(ModelChatLog::getCreateDate, DateUtil.offsetDay(DateTime.now(), offsetDay));
        return baseMapper.delete(queryWrapper);
    }

    @Override
    public List<ModelChatLog> queryLastSendTimeByModelId(List<Long> modelIdList) {
        List<ModelChatLog> result = new ArrayList<>();
        if (CollUtil.isEmpty(modelIdList)) {
            return result;
        }
        List<List<Long>> partions = CollUtil.split(modelIdList, 500);
        partions.forEach(list -> {
            List<ModelChatLog> logs = baseMapper.queryLastSendTimeByModelId(list);
            if (CollUtil.isNotEmpty(logs)) {
                result.addAll(logs);
            }
        });
        return result;
    }

    @Override
    public List<ModelChatLog> queryExpiredDeployTask(Integer offsetDay) {
        return baseMapper.queryExpiredDeployTask(offsetDay);
    }

    @Override
    public List<ModelChatLogVO> queryUserLastTenLogs(Long modelChatId) {
        QueryParam queryParam = new QueryParam();
        queryParam.setPageParam(PageParam.DEFAULT_PAGE_PARAM);
        queryParam.setFilterMap(MapUtil.of("modelChatId", modelChatId));
        queryParam.setSortingFields(CollUtil.newArrayList(SortingField.desc(BaseModel.CREATE_TIME_FIELD_NAME)));
        PageResult<ModelChatLogVO> pageResult = super.queryPage(queryParam);
        return CollUtil.reverse(pageResult.getRows());
    }

    @Override
    public List<HourlyTokenStats> getHourlyTokenStats(QueryHourlyStatsDTO queryParam) {
        return baseMapper.getHourlyTokenStats(queryParam.getModelId(), queryParam.getStartTime(), queryParam.getEndTime());
    }

    @Override
    public List<HourlyCallStats> getHourlyCallStats(QueryHourlyStatsDTO queryParam) {
        return baseMapper.getHourlyCallStats(queryParam.getModelId(), queryParam.getStartTime(), queryParam.getEndTime());
    }

    @Override
    public ModelCallSummary getModelCallSummary(Long modelId) {
        return baseMapper.getModelCallSummary(modelId);
    }
}