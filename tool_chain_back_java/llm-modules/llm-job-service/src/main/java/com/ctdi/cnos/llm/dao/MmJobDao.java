package com.ctdi.cnos.llm.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctdi.cnos.llm.beans.job.MmJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调度任务信息 数据层
 *
 * @author huangjinhua
 * @since 2024/6/3
 */
@Mapper
public interface MmJobDao extends BaseMapper<MmJob> {

}
