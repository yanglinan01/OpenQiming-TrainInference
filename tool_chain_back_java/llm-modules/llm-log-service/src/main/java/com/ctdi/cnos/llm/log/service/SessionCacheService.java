package com.ctdi.cnos.llm.log.service;

import com.ctdi.cnos.llm.beans.log.sessioncache.SessionCacheVO;
import com.ctdi.cnos.llm.beans.log.sessioncache.SessionRequestDTO;

public interface SessionCacheService {
     void createOrUpdateSession(SessionRequestDTO requestDTO);
     SessionCacheVO getSession(String sessionId, String intention,String province);
     void deleteSession(String sessionId, String intention,String province);
     int cleanHistoryData();
}
