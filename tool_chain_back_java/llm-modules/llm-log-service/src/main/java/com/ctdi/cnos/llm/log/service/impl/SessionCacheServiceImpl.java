package com.ctdi.cnos.llm.log.service.impl;

import com.ctdi.cnos.common.core.utils.bean.BeanUtils;
import com.ctdi.cnos.llm.beans.log.sessioncache.SessionCacheEntity;
import com.ctdi.cnos.llm.beans.log.sessioncache.SessionCacheVO;
import com.ctdi.cnos.llm.beans.log.sessioncache.SessionRequestDTO;
import com.ctdi.cnos.llm.log.dao.SessionCacheDao;
import com.ctdi.cnos.llm.log.service.SessionCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("sessionCacheService")
public class SessionCacheServiceImpl implements SessionCacheService {
    @Autowired
    private SessionCacheDao sessionCacheDao;

    @Override
    public void createOrUpdateSession(SessionRequestDTO requestDTO) {
        // 查询是否存在该会话记录
        SessionCacheEntity existingSession = sessionCacheDao.getSession(requestDTO.getSessionId(), requestDTO.getIntention(), requestDTO.getProvince());

        if (existingSession == null) {
            // 如果记录不存在，则插入新记录，会话次数为 0
            SessionCacheEntity newSession = new SessionCacheEntity();
            newSession.setId(UUID.randomUUID().toString());
            newSession.setSessionId(requestDTO.getSessionId());
            newSession.setIntention(requestDTO.getIntention());
            newSession.setProvince(requestDTO.getProvince());
            newSession.setSessionNum(0); // 初始会话次数为 0
            sessionCacheDao.createOrUpdateSession(newSession);
        } else {
            // 如果记录存在，则更新会话次数（由 SQL 自动处理）
            sessionCacheDao.createOrUpdateSession(existingSession);
        }
    }

    @Override
    public SessionCacheVO getSession(String sessionId, String intention,String province) {
        SessionCacheEntity SessionCacheEntity = sessionCacheDao.getSession(sessionId, intention,province);
        if (SessionCacheEntity == null) {
            return null;
        }

        // 转换为 VO 对象
        SessionCacheVO sessionVO = new SessionCacheVO();
        BeanUtils.copyProperties(SessionCacheEntity, sessionVO);

        return sessionVO;
    }

    @Override
    public void deleteSession(String sessionId, String intention,String province) {
        sessionCacheDao.deleteSession(sessionId, intention, province);
    }

    @Override
    public int cleanHistoryData() {
        return sessionCacheDao.cleanHistoryData();
    }
}
