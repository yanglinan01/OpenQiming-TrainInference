package com.ctdi.cnos.llm.feign.log;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.log.sessioncache.SessionCacheVO;
import com.ctdi.cnos.llm.beans.log.sessioncache.SessionRequestDTO;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(value = RemoteConstont.LOG_SERVICE_NAME, path = "${cnos.server.llm-log-service.contextPath}")
public interface SessionCacheServiceClientFegin {
    /**
     * 创建或更新会话。
     */
    @PostMapping("/sessionCache/save")
    OperateResult<String> createOrUpdateSession(@RequestBody SessionRequestDTO requestDTO);

    /**
     * 获取会话信息。
     */
    @GetMapping("/sessionCache/get")
    OperateResult<SessionCacheVO>  getSession(
            @RequestParam("sessionId") String sessionId,
            @RequestParam("intention") String intention,
            @RequestParam("province") String province
    );

    /**
     * 删除会话。
     */
    @DeleteMapping("/sessionCache/delete")
    OperateResult<String> deleteSession(
            @RequestParam("sessionId") String sessionId,
            @RequestParam("intention") String intention,
            @RequestParam("province") String province
    );
    @PostMapping("/sessionCache/cleanHistoryData")
    int cleanHistoryData();
}
