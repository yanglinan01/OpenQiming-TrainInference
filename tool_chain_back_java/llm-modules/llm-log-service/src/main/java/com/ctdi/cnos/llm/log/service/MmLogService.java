package com.ctdi.cnos.llm.log.service;

import com.ctdi.cnos.llm.beans.log.MmLog;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yuyong
 * @date 2024/4/3 17:14
 */
public interface MmLogService {

    /**
     * 根据id删除日志记录信息
     * @param id
     * @return
     */
    int deleteById(BigDecimal id);

    /**
     * 新增日志记录信息
     * @param mmLog
     * @return
     */
    int insert(MmLog mmLog);

    /**
     * 根据id查询日志记录信息
     * @param id
     * @return
     */
    MmLog queryById(BigDecimal id);

    /**
     * 修改日志记录信息
     * @param mmLog
     * @return
     */
    int updateById(MmLog mmLog);

    /**
     * 查询日志记录信息
     * @param mmLog
     * @return
     */
    List<MmLog> queryList (MmLog mmLog);

    /**
     * 查询日志记录数量
     * @param mmLog
     * @return
     */
    int count(MmLog mmLog);

    /**
     * 分页查询日志记录信息
     * @param page
     * @param mmLog
     * @return
     */
    List<MmLog> queryList (Page<MmLog> page, MmLog mmLog);
}
