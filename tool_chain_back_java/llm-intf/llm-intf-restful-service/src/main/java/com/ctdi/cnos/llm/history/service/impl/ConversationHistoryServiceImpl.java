/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.history.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.ctdi.cnos.llm.beans.log.conversation.ConversationHistory;
import com.ctdi.cnos.llm.beans.log.conversation.ConversationHistoryVO;
import com.ctdi.cnos.llm.history.dao.ConversationHistoryDao;
import com.ctdi.cnos.llm.history.service.ConversationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 历史记录 业务接口实现
 *
 * @author huangjinhua
 * @since 2024/5/10
 */
@Service
@RequiredArgsConstructor
public class ConversationHistoryServiceImpl implements ConversationHistoryService {
    private final ConversationHistoryDao historyDao;

    @Override
    public List<ConversationHistoryVO> queryList(String userId, String sessionId, Integer num) {
        List<ConversationHistoryVO> result = CollUtil.newArrayList();
        List<ConversationHistory> list = historyDao.queryList(userId, sessionId, num);
        if (list != null && !list.isEmpty()) {
            list.forEach(conversationHistory -> {
                ConversationHistoryVO historyVO = new ConversationHistoryVO();
                BeanUtil.copyProperties(conversationHistory, historyVO);
                if (Objects.nonNull(conversationHistory.getUserStateText())) {
                    historyVO.setUserState(JSON.parseObject(conversationHistory.getUserStateText()));
                }
                result.add(historyVO);
            });
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(ConversationHistory history) {
        if (Objects.isNull(history.getId())) {
            history.setId(IdUtil.getSnowflakeNextId());
        }
        if (Objects.nonNull(history.getUserState())) {
            history.setUserStateText(JSON.toJSONString(history.getUserState()));
        } else {
            history.setUserStateText("{}");
        }
        return historyDao.insert(history);
    }

    @Override
    public int cleanHistoryData(LocalDate localDate) {
        java.util.Date date = Date.from(localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
        int i = historyDao.deleteByTime(date);
        return i;
    }
}
