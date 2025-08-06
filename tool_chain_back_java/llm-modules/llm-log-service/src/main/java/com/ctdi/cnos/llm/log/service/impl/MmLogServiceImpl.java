package com.ctdi.cnos.llm.log.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.log.MmLog;
import com.ctdi.cnos.llm.log.dao.MmLogDao;
import com.ctdi.cnos.llm.log.service.MmLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yuyong
 * @date 2024/4/3 17:16
 */
@Service
public class MmLogServiceImpl implements MmLogService {

    @Autowired
    private MmLogDao mmLogDao;

    @Override
    public int deleteById(BigDecimal id) {
        return mmLogDao.deleteById(id);
    }

    @Override
    public int insert(MmLog mmLog) {
        return mmLogDao.insert(mmLog);
    }

    @Override
    public MmLog queryById(BigDecimal id) {
        return mmLogDao.queryById(id);
    }

    @Override
    public int updateById(MmLog mmLog) {
        return mmLogDao.updateById(mmLog);
    }

    @Override
    public List<MmLog> queryList(MmLog mmLog) {
        return mmLogDao.queryList(mmLog);
    }

    @Override
    public int count(MmLog mmLog) {
        return mmLogDao.count(mmLog);
    }

    @Override
    public List<MmLog> queryList(Page<MmLog> page, MmLog mmLog) {
        return mmLogDao.queryList(page, mmLog);
    }
}
