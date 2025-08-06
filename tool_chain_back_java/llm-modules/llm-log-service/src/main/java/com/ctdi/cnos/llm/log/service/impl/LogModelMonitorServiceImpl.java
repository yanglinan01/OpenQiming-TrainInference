package com.ctdi.cnos.llm.log.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctdi.cnos.llm.beans.log.MmModelMonitorIntf;
import com.ctdi.cnos.llm.beans.log.MmModelMonitorModel;
import com.ctdi.cnos.llm.beans.log.MmModelMonitorStatistics;
import com.ctdi.cnos.llm.beans.log.ModelCallDataReq;
import com.ctdi.cnos.llm.log.dao.MmModelMonitorIntfDao;
import com.ctdi.cnos.llm.log.dao.MmModelMonitorModelDao;
import com.ctdi.cnos.llm.log.dao.MmModelMonitorStatisticsDao;
import com.ctdi.cnos.llm.log.service.LogModelMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuangGuanSheng
 * @date 2024-07-04 14:42
 */

@Service
@Slf4j
public class LogModelMonitorServiceImpl implements LogModelMonitorService {

    @Resource
    private MmModelMonitorModelDao mmModelMonitorModelDao;

    @Resource
    private MmModelMonitorIntfDao mmModelMonitorIntfDao;

    @Resource
    private MmModelMonitorStatisticsDao mmModelMonitorStatisticsDao;

    @Override
    public Map<String, Object> insertModelCallData(ModelCallDataReq req) {
        Map<String, Object> resultMap = new HashMap<>();
        Assert.notNull(req.getTaskId(), "训练ID不能为空");

        Date now = new Date();
        Integer yyyyMMdd = Integer.parseInt(DateUtil.format(now, "yyyyMMdd"));
        Integer hours = Integer.parseInt(DateUtil.format(now,"HH"));

        MmModelMonitorModel model = new MmModelMonitorModel();
        BeanUtils.copyProperties(req, model);
        model.setModelCallDate(now);
        model.setModelCallDateDays(yyyyMMdd);
        model.setModelCallDateHours(hours);
        mmModelMonitorModelDao.insert(model);

        MmModelMonitorIntf intf = new MmModelMonitorIntf();
        intf.setTaskId(req.getTaskId());
        intf.setIntfCallDate(now);
        intf.setIntfCallType(req.getSuccessFlag());
        intf.setRemark(req.getRemark());
        intf.setIntfCallCounts(1L);
        intf.setIntfCallDateDays(yyyyMMdd);
        intf.setIntfCallDateHours(hours);
        mmModelMonitorIntfDao.insert(intf);

        MmModelMonitorStatistics mmModelMonitorStatistics = mmModelMonitorStatisticsDao.selectData(req.getTaskId());
        if (mmModelMonitorStatistics == null) {
            mmModelMonitorStatistics = new MmModelMonitorStatistics();
            mmModelMonitorStatistics.setTaskId(req.getTaskId());
            mmModelMonitorStatistics.setIntfTotal(1L);
            mmModelMonitorStatistics.setTokenInput(req.getModelInputToken());
            mmModelMonitorStatistics.setTokenOutput(req.getModelOutputToken());
            mmModelMonitorStatistics.setTokenTotal(req.getModelInputToken()+req.getModelOutputToken());
            mmModelMonitorStatisticsDao.insert(mmModelMonitorStatistics);
        } else {
            mmModelMonitorStatisticsDao.updateCounts(req.getTaskId(),req.getModelInputToken(),req.getModelOutputToken());
        }

        return resultMap;
    }

    @Override
    public String deleteMoreThanTimeData() {
        LocalDate now = LocalDate.now();
        LocalDate fifteenDaysAgo = now.minus(14, ChronoUnit.DAYS);

        LambdaQueryWrapper<MmModelMonitorIntf> lIntf = new LambdaQueryWrapper<>();
        lIntf.lt(MmModelMonitorIntf::getIntfCallDate,fifteenDaysAgo);
        mmModelMonitorIntfDao.delete(lIntf);

        LambdaQueryWrapper<MmModelMonitorModel> lModel = new LambdaQueryWrapper<>();
        lModel.lt(MmModelMonitorModel::getModelCallDate,fifteenDaysAgo);
        mmModelMonitorModelDao.delete(lModel);

        return "success";
    }
}
