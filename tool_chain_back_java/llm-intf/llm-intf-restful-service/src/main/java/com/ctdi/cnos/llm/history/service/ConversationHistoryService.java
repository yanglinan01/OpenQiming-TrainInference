package com.ctdi.cnos.llm.history.service;

import com.ctdi.cnos.llm.beans.log.conversation.ConversationHistory;
import com.ctdi.cnos.llm.beans.log.conversation.ConversationHistoryVO;

import java.time.LocalDate;
import java.util.List;

/**
 * 历史记录 业务接口
 *
 * @author huangjinhua
 * @since 2024/5/10
 */
public interface ConversationHistoryService {

    /**
     * 查询历史记录
     * @param userId 用户ID
     * @param sessionId 会话ID
     * @param num 要查询的历史记录条数
     * @return List<ConversationHistoryVO>
     */
    List<ConversationHistoryVO> queryList(String  userId, String sessionId, Integer num);

    /**
     * 新增 历史记录
     *
     * @param history 历史记录对象
     * @return int
     */
    int insert(ConversationHistory history);

    /**
     * 清理ConversationHistory数据
     * @return
     */
    int cleanHistoryData(LocalDate date);
}
