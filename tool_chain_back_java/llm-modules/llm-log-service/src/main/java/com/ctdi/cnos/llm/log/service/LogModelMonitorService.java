package com.ctdi.cnos.llm.log.service;

import com.ctdi.cnos.llm.beans.log.ModelCallDataReq;

import java.util.Map;

/**
 * @author HuangGuanSheng
 * @date 2024-07-04 14:42
 */
public interface LogModelMonitorService {
    Map<String, Object> insertModelCallData(ModelCallDataReq req);

    String deleteMoreThanTimeData();
}
