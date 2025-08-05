package com.ctdi.cnos.llm.count.controller;

import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.beans.api.FeedbackSummaryDTO;
import com.ctdi.cnos.llm.beans.api.Record;
import com.ctdi.cnos.llm.feign.count.ApiServiceClientFegin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author yuyong
 * @date 2024/5/27 11:00
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/model")
@Api(tags = {"api接口"})
@Slf4j
public class ApiController {

    @Autowired
    private ApiServiceClientFegin apiServiceClientFegin;

    @AuthIgnore
    @PostMapping("/call_stats_v2")
    public Map<String, Object> getStatisticalDataByTime(@ApiIgnore @RequestBody(required = false) Map<String, String> map) {
        Map<String, Object> result = apiServiceClientFegin.getStatisticalDataByTime(map);
        return result;
    }

    @AuthIgnore
    @PostMapping("/call_stats_v3")
    public Map<String, Object> getAllStatisticalDataByTime(@ApiIgnore @RequestBody(required = false) Map<String, String> map) {
        Map<String, Object> result = apiServiceClientFegin.getAllStatisticalDataByTime(map);
        return result;
    }

    @AuthIgnore
    @PostMapping("/call_stats_v4")
    public Map<String, Object> getRecordCount(@ApiIgnore @RequestBody(required = false) Map<String, String> map) {
        Map<String, Object> result = apiServiceClientFegin.getRecordCount(map);
        return result;
    }

    @AuthIgnore
    @PostMapping("/getFeedBackList")
    public Map<String, Object> getFeedBackList(@ApiIgnore @RequestBody(required = false) Map<String, String> map) {
        Map<String, Object> result = apiServiceClientFegin.getFeedBackList(map);
        return result;
    }

    @AuthIgnore
    @PostMapping("/addRecord")
    public Map<String, Object> addRecord(@ApiIgnore @RequestBody(required = false) Record record) {
        Map<String, Object> result = apiServiceClientFegin.addRecord(record);
        return result;
    }

    @AuthIgnore
    @PostMapping("/model_performance_data")
    public Map<String, Object> getObtainStatisticalData(@ApiIgnore @RequestBody(required = false) Map<String, String> map) {
        Map<String, Object> result = apiServiceClientFegin.getObtainStatisticalData(map);
        return result;
    }

    @AuthIgnore
    @ApiOperation(value = "统计网络大模型赞踩统计", notes = "用于网络大模型在各个省份下各个场景赞踩情况的统计。省份、场景（助手）、总反馈数、点赞数、点踩数及点赞百分比")
    @PostMapping("/feedBackSummary")
    public JSONObject getFeedbackSummary(@RequestBody
                                                                                FeedbackSummaryDTO dto) {
        return apiServiceClientFegin.getFeedbackSummary(dto);
    }
}