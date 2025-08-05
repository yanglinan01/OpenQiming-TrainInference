package com.ctdi.cnos.llm.log.service;


import java.util.Map;

/**
 * @author yuyong
 * @date 2024/11/18 16:19
 */
public interface MmTrainLogService {

    /**
     * 根据训练id分页查询训练日志
     * @param pageNum
     * @param pageSize
     * @param id
     * @return
     */
    Map<String, Object> queryTrainLog(int pageNum, int pageSize, Long id);
}
