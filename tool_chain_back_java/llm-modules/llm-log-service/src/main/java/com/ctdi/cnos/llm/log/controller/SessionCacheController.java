package com.ctdi.cnos.llm.log.controller;

import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.beans.log.sessioncache.SessionCacheVO;
import com.ctdi.cnos.llm.beans.log.sessioncache.SessionRequestDTO;
import com.ctdi.cnos.llm.log.service.SessionCacheService;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "会话日志接口", value = "SessionCacheController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/sessionCache")
public class SessionCacheController {

    private static final Logger log = LoggerFactory.getLogger(SessionCacheController.class);
    @Autowired
    private SessionCacheService sessionCacheService;

    @PostMapping("/save")
    @AuthIgnore
    public OperateResult<String> createOrUpdateSession(@RequestBody SessionRequestDTO requestDTO) {
        sessionCacheService.createOrUpdateSession(requestDTO);
        return OperateResult.success(null,"成功");
    }

    @GetMapping("/get")
    @AuthIgnore
    public OperateResult<SessionCacheVO> getSession(@RequestParam String sessionId, @RequestParam String intention,@RequestParam String province) {
        SessionCacheVO sessionCacheVO = sessionCacheService.getSession(sessionId, intention,province);
        return OperateResult.success(sessionCacheVO,"成功");
    }

    @DeleteMapping("/delete")
    @AuthIgnore
    public OperateResult<String> deleteSession(@RequestParam String sessionId, @RequestParam String intention,@RequestParam String province) {
        sessionCacheService.deleteSession(sessionId, intention,province);
        return OperateResult.success(null,"成功");
    }
    @PostMapping("/cleanHistoryData")
    @AuthIgnore
    public int cleanHistoryData(){
        int count = sessionCacheService.cleanHistoryData();
        log.info("SessionCacheController会话缓存日志接口,清除"+count+"历史数据");
        return count;
    }
}
