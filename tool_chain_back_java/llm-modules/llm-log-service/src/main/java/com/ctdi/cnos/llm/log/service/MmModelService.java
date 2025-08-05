package com.ctdi.cnos.llm.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.log.MmModelMonitorStatistics;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorIntfListReq;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorIntfReq;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorModelReq;
import com.ctdi.cnos.llm.beans.log.model.rsp.MmModelMonitorModelListVO;
import com.ctdi.cnos.llm.beans.log.model.rsp.MmModelMonitorModelVO;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author caojunhao
 * @DATE 2024/7/4
 */
public interface MmModelService {

    /**
     * 日志概括
     * @return
     */
    MmModelMonitorStatistics queryStatistics();
    /**
     * 模型调用统计
     * @return
     */
    List<MmModelMonitorModelVO> queryModelRequest(MmModelMonitorModelReq req) throws ExecutionException, InterruptedException;

    /**
     * 模型调用统计 统计图
     * @return
     */
    List<MmModelMonitorModelVO> queryModelRequestChart(MmModelMonitorIntfReq req) throws ExecutionException, InterruptedException;

    /**
     * 模型调用统计 列表
     *
     * @return
     */
    Page<MmModelMonitorModelListVO> queryIntfPage(Page<MmModelMonitorModelListVO> page, MmModelMonitorIntfListReq req,String deployBelong) throws Exception;

}
