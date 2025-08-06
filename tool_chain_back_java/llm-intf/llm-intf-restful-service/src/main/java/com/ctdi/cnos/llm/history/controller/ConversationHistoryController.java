/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.history.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.beans.log.conversation.ConversationHistory;
import com.ctdi.cnos.llm.beans.log.conversation.ConversationHistoryVO;
import com.ctdi.cnos.llm.history.service.ConversationHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史记录接口 Controller
 *
 * @author huangjinhua
 * @since 2024/5/10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/history")
@Api(tags = {"历史记录接口"})
@Slf4j
public class ConversationHistoryController {
    private final ConversationHistoryService historyService;

    @ApiOperation(value = "查询历史列表", notes = "查询历史列表")
    @PostMapping("/read")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "num", value = "要查询的历史记录条数，默认为5", paramType = "body"),
            @ApiImplicitParam(name = "session_id", value = "会话ID", required = true, paramType = "body"),
            @ApiImplicitParam(name = "user_id", value = "用户ID", required = true, paramType = "body")
    })
    @AuthIgnore
    public Map<String, Object> queryList(@ApiIgnore @RequestBody Map<String, Object> map) {

        Map<String, Object> result = new HashMap<>(6);
        try {
            int code;
            String msg;
            Integer num = MapUtil.getInt(map, "num", 5);
            String userId = MapUtil.getStr(map, "user_id", "");
            String sessionId = MapUtil.getStr(map, "session_id", "");
            if (CharSequenceUtil.isNotBlank(userId) && CharSequenceUtil.isNotBlank(sessionId)) {
                List<ConversationHistoryVO> list = historyService.queryList(userId, sessionId, num);
                result.put("history_list", list);
                result.put("session_id", sessionId);
                result.put("user_id", userId);
                code = 0;
                msg = "成功生成回答";
            } else {
                code = 10001;
                msg = "查询参数缺失！请核查session_id、user_id";
                log.warn(msg);
            }
            result.put("code", code);
            result.put("msg", msg);
        } catch (Exception e) {
            log.error("历史数据库查询异常", e);
            result.put("code", 10001);
            result.put("msg", "历史数据库查询失败");
        }
        return result;
    }


    @ApiOperation(value = "写入历史", notes = "写入历史")
    @PostMapping("/write")
    @AuthIgnore
    public Map<String, Object> add(@RequestBody ConversationHistory history) {

        Map<String, Object> result = new HashMap<>(2);
        try {
            historyService.insert(history);
            result.put("code", 0);
            result.put("msg", "写入历史对话数据库成功");
        } catch (Exception e) {
            log.error("写入历史对话数据库异常", e);
            result.put("code", 10001);
            result.put("msg", "写入历史对话数据库失败");
        }
        return result;
    }

    @ApiOperation(value = "清除历史数据", notes = "清除历史数据")
    @PostMapping("/clean")
    @AuthIgnore
    public String cleanHistoryData(){
        log.info("----------------开始清除cleanHistoryData----------------");
        LocalDate now = LocalDate.now();
        LocalDate sevenDaysAgo = now.minus(7, ChronoUnit.DAYS);
        int i=historyService.cleanHistoryData(sevenDaysAgo);
        log.info("----------------结束清除cleanHistoryData----------------");
        return "清理历史数据成功，"+i+"条";
    }

}
