package com.ctdi.cnos.llm.log;

import com.alibaba.fastjson.JSON;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.beans.log.sessioncache.SessionCacheVO;
import com.ctdi.cnos.llm.beans.log.sessioncache.SessionRequestDTO;
import com.ctdi.cnos.llm.feign.log.SessionCacheServiceClientFegin;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/sessionCache")
@Api(tags = {"会话记录接口"})
@Slf4j
public class SessionCacheController {
    @Autowired
    private SessionCacheServiceClientFegin sessionCacheServiceClientFegin;

    @PostMapping("/save")
    @AuthIgnore
    public OperateResult<String> createOrUpdateSession(@RequestBody SessionRequestDTO requestDTO) {
        log.info("新增会话日志接口:{}", JSON.toJSONString(requestDTO));

        return sessionCacheServiceClientFegin.createOrUpdateSession(requestDTO);
    }

    @GetMapping("/get")
    @AuthIgnore
    public OperateResult<SessionCacheVO> getSession(@RequestParam String sessionId, @RequestParam String intention,
                                                     @RequestParam String province) {
        return sessionCacheServiceClientFegin.getSession(sessionId, intention,province);
    }

    @DeleteMapping("/delete")
    @AuthIgnore
    public OperateResult<String> deleteSession(@RequestParam String sessionId, @RequestParam String intention,
                                                @RequestParam String province) {
        return sessionCacheServiceClientFegin.deleteSession(sessionId, intention,province);
    }
}
