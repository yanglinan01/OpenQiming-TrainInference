package com.ctdi.cnos.llm.feign.count;

import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.api.FeedbackSummaryDTO;
import com.ctdi.cnos.llm.beans.api.FeedbackSummaryVO;
import com.ctdi.cnos.llm.beans.api.Record;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author yuyong
 * @date 2024/5/27 10:53
 */
@Component
@FeignClient(value = RemoteConstont.API_SERVICE_NAME, path = "${cnos.server.llm-api-service.contextPath}")
public interface ApiServiceClientFegin {

    @PostMapping(value = "/model/call_stats_v3")
    public Map<String, Object> getStatisticalDataByTime(@RequestBody(required = false) Map<String,String> map);

    @PostMapping(value = "/model/call_stats_v3")
    public Map<String, Object> getAllStatisticalDataByTime(@RequestBody(required = false) Map<String,String> map);

    @PostMapping(value = "/model/call_stats_v4")
    public Map<String, Object> getRecordCount(@RequestBody(required = false) Map<String,String> map);

    @PostMapping(value = "/model/getFeedBackList")
    public Map<String, Object> getFeedBackList(@RequestBody(required = false) Map<String,String> map);

    @PostMapping(value = "/model/addRecord")
    public Map<String, Object> addRecord(@RequestBody(required = false) Record record);

    @PostMapping(value = "/model/getObtainStatisticalData")
    public Map<String, Object> getObtainStatisticalData(@RequestBody(required = false) Map<String,String> map);

    @PostMapping(value = "/model/feedBackSummary")
    JSONObject getFeedbackSummary(@RequestBody FeedbackSummaryDTO dto);

}