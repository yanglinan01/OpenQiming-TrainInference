package com.ctdi.cnos.llm.feign.log;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.log.MmLog;
import com.ctdi.cnos.llm.beans.log.MmModelMonitorStatistics;
import com.ctdi.cnos.llm.beans.log.ModelCallDataReq;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorIntfListReq;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorIntfReq;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorModelReq;
import com.ctdi.cnos.llm.beans.log.model.rsp.MmModelMonitorModelVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author yuyong
 * @date 2024/4/10 9:42
 */
@Component
@FeignClient(value = RemoteConstont.LOG_SERVICE_NAME, path = "${cnos.server.llm-log-service.contextPath}")
public interface LogServiceClientFeign {

    @PostMapping(value = "/log/queryList")
    public Map<String, Object> queryList(@RequestBody(required = false) MmLog mmLog);

    @PostMapping(value = "/log/queryPage")
    public Map<String, Object> queryPage(@RequestParam("pageSize") long pageSize,
                                         @RequestParam("currentPage")long currentPage,
                                         @RequestBody(required = false) MmLog mmLog);
    @PostMapping(value = "/log/addLog")
    public Map<String, Object> addLog(@RequestBody(required = false) MmLog mmLog);

    @PostMapping(value = "/log/dataAssembly")
    public MmLog dataAssembly(@RequestParam("createId") String createId, @RequestParam("modifierId") String modifierId, @RequestParam("interfaceName") String interfaceName);

    @PostMapping(value = "/logModelMonitor/modelCallData")
    public Map<String, Object> modelCallData(@RequestBody(required = false) ModelCallDataReq req);

    @PostMapping(value = "/logModelMonitor/deleteMoreThanTimeData")
    void deleteMoreThanTimeData();
    @PostMapping(value = "/logModel/queryStatistics")
    MmModelMonitorStatistics queryStatistics();

    @PostMapping(value = "/logModel/queryModelRequest")
    List<MmModelMonitorModelVO> queryModelRequest(@RequestBody MmModelMonitorModelReq req);

    @PostMapping(value = "/logModel/queryModelRequestChart")
    List<MmModelMonitorModelVO> queryModelRequestChart(@RequestBody MmModelMonitorIntfReq req);

    @PostMapping(value = "/logModel/queryModelRequestList")
    Map<String, Object> queryModelRequestList(@RequestBody(required = false) MmModelMonitorIntfListReq req);

    @PostMapping(value = "/trainLog/queryMmTrainLogList")
    Map<String, Object> queryMmTrainLogList(@RequestBody(required = false) Map<String, String> map);
}
