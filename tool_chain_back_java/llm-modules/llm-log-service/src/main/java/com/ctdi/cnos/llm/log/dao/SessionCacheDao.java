package com.ctdi.cnos.llm.log.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.log.sessioncache.SessionCacheEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SessionCacheDao extends BaseDaoMapper<SessionCacheEntity> {
    /**
     * 根据会话ID、省份、意图查询记录。
     */
    SessionCacheEntity getSession(@Param("sessionId") String sessionId,@Param("intention") String intention, @Param("province") String province);

    /**
     * 插入或更新会话记录。
     */
    void createOrUpdateSession(SessionCacheEntity sessionCacheEntity);

    /**
     * 删除指定会话记录。
     */
    void deleteSession(@Param("sessionId") String sessionId,@Param("intention") String intention, @Param("province") String province);

    int cleanHistoryData();
}
