/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.history.dao;

import com.ctdi.cnos.llm.beans.log.conversation.ConversationHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 历史记录 Dao
 *
 * @author huangjinhua
 * @since 2024/5/10
 */
@Mapper
public interface ConversationHistoryDao {

    /**
     * 查询历史记录
     * @param userId 用户ID
     * @param sessionId 会话ID
     * @param num 要查询的历史记录条数
     * @return List<ConversationHistory>
     */
    List<ConversationHistory> queryList(@Param("userId") String userId, @Param("sessionId") String sessionId, @Param("num") Integer num);

    /**
     * 新增历史
     *
     * @param conversationHistory 历史对象
     * @return int
     */
    int insert(ConversationHistory conversationHistory);

    int deleteByTime(@Param("date") Date date);
}
