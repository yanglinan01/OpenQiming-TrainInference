package com.ctdi.cnos.llm.log.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.log.MmModelMonitorIntf;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorIntfListReq;
import com.ctdi.cnos.llm.beans.log.model.rsp.MmModelMonitorModelListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author HuangGuanSheng
 * @date 2024-07-04 11:12
 */

@Mapper
public interface MmModelMonitorIntfDao  extends BaseMapper<MmModelMonitorIntf> {
    Page<MmModelMonitorModelListVO> queryList(Page<MmModelMonitorModelListVO> page, @Param("req") MmModelMonitorIntfListReq req);
}
