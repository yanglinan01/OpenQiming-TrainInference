package com.ctdi.cnos.llm.log.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctdi.cnos.llm.beans.log.MmModelMonitorStatistics;
import com.ctdi.cnos.llm.beans.log.model.rsp.MmModelMonitorModelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author HuangGuanSheng
 * @date 2024-07-04 11:12
 */

@Mapper
public interface MmModelMonitorStatisticsDao extends BaseMapper<MmModelMonitorStatistics> {
    MmModelMonitorStatistics selectData(@Param("taskId") Long taskId);
    MmModelMonitorStatistics queryLastStatistics(@Param("taskIds") List<Long> taskIds);

    /**
     * 查询模型调用统计
     *
     * @param begin
     * @param end
     * @param taskId
     * @param modelCallType
     * @return
     */
    List<MmModelMonitorModelVO> queryModelRequest(@Param("begin") String begin, @Param("end") String end, @Param("taskId")Long taskId, @Param("modelCallType")Integer modelCallType);

    void updateCounts(@Param("taskId") Long taskId, @Param("inputToken") Long modelInputToken, @Param("outputToken")Long modelOutputToken);

    /**
     * 查询接口调用统计
     *
     * @param begin
     * @param end
     * @param taskId
     * @param intfCallType
     * @return
     */
    List<MmModelMonitorModelVO> queryIntfRequest(@Param("begin") String begin, @Param("end") String end, @Param("taskId")Long taskId, @Param("intfCallType")Integer intfCallType);

}
