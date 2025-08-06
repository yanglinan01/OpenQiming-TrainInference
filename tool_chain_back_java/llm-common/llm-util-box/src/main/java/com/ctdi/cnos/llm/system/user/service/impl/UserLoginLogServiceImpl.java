package com.ctdi.cnos.llm.system.user.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.system.entity.UserLoginLog;
import com.ctdi.cnos.llm.system.user.dao.UserLoginLogDao;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.system.user.service.UserLoginLogService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户登录日志 数据操作服务类
 *
 * @author laiqi
 * @since 2024/10/15
 */
public class UserLoginLogServiceImpl extends BaseService<UserLoginLogDao, UserLoginLog, UserLoginLog> implements UserLoginLogService {

    public UserLoginLogServiceImpl(UserLoginLogDao baseMapper) {
        super.baseMapper = baseMapper;
    }
    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<UserLoginLog> wrapper, QueryParam queryParam) {
        UserLoginLog filter = queryParam.getFilterDto(UserLoginLog.class);
    }

    @Override
    public boolean isUserLoginToday(Long userId) {
        return this.baseMapper.selectOne(UserLoginLog::getUserId, userId, UserLoginLog::getLoginDate, LocalDate.now()) != null;
    }

    @Override
    public void saveUserLoginLog(UserVO userVO) {
        /* if (!isUserLoginToday(userVO.getId())) {
            UserLoginLog loginLog = new UserLoginLog();
            loginLog.setUserId(userVO.getId());
            loginLog.setRegionCode(userVO.getRegionCode());
            loginLog.setLoginDate(LocalDate.now());
            save(loginLog);
        } */
        // 登录一次统计一次
        UserLoginLog loginLog = new UserLoginLog();
        loginLog.setUserId(userVO.getId());
        loginLog.setRegionCode(userVO.getRegionCode());
        loginLog.setLoginDate(DateTime.now());
        save(loginLog);
    }


    /**
     * 统计当前日期近31天每天的日活用户数
     *
     * @return 日期：活跃人数
     */
    @Override
    public Map<String, Long> countActiveUsersForLast31Days() {
        DateTime today = DateUtil.beginOfDay(DateUtil.date());
        DateTime startDate = DateUtil.offsetDay(today, -30);
        today = DateUtil.endOfDay(today);
        // 生成日期范围
        List<String> dateRange = generateDateRange(startDate, today);

        // 查询实际的登录数据
        List<Map<String, Object>> resultList = this.baseMapper.countActiveUsersForLast31Days(startDate, today);

        // 处理查询结果并填充缺失日期
        return fillMissingDates(resultList, dateRange);
    }

    /**
     * 统计当前日期前12个月每月的日活用户数
     *
     * @return 月份：活跃人数
     */
    @Override
    public Map<String, Long> countActiveUsersForLast12Months() {
        DateTime today = DateUtil.beginOfDay(DateUtil.date());
        DateTime startDate = DateUtil.offsetMonth(today, -11);
        today = DateUtil.endOfDay(today);

        // 生成月份范围
        List<String> monthRange = generateMonthRange(startDate, today);

        // 查询实际的登录数据
        List<Map<String, Object>> resultList = this.baseMapper.countActiveUsersForLast12Months(startDate, today);

        // 处理查询结果并填充缺失月份

        return fillMissingMonths(resultList, monthRange);
    }

    private List<String> generateDateRange(DateTime startDate, DateTime endDate) {
        List<String> dates = new ArrayList<>();
        DateTime current = startDate;
        while (!current.isAfter(endDate)) {
            dates.add(DateUtil.formatDate(current));
            current = DateUtil.offsetDay(current, 1);
        }
        return dates;
    }

    private List<String> generateMonthRange(DateTime startDate, DateTime endDate) {
        List<String> months = new ArrayList<>();
        DateTime current = startDate;
        while (!current.isAfter(endDate)) {
            months.add(DateUtil.format(current, DatePattern.NORM_MONTH_FORMATTER));
            current = DateUtil.offsetMonth(current, 1);
        }
        return months;
    }

    private Map<String, Long> fillMissingDates(List<Map<String, Object>> resultList, List<String> dateRange) {
        Map<String, Long> filledMap = new LinkedHashMap<>();

        // 将查询结果转换为 Map
        Map<String, Long> queryResults = resultList.stream()
                .collect(Collectors.toMap(
                        map -> (String) map.get("login_date"),
                        map -> (Long) map.get("count"),
                        (oldValue, newValue) -> oldValue
                ));

        // 填充缺失日期
        for (String date : dateRange) {
            Long count = queryResults.getOrDefault(date, 0L);
            filledMap.put(date, count);
        }

        return filledMap;
    }

    private Map<String, Long> fillMissingMonths(List<Map<String, Object>> resultList, List<String> monthRange) {
        Map<String, Long> filledMap = new LinkedHashMap<>();

        // 将查询结果转换为 Map
        Map<String, Long> queryResults = resultList.stream()
                .collect(Collectors.toMap(
                        map -> (String) map.get("month"),
                        map -> (Long) map.get("count"),
                        (oldValue, newValue) -> oldValue
                ));

        // 填充缺失月份
        for (String month : monthRange) {
            Long count = queryResults.getOrDefault(month, 0L);
            filledMap.put(month, count);
        }

        return filledMap;
    }

    @Override
    public OperateResult<Map<String, Long>> queryChart(StatType type) {
        switch (type) {
            case DAY:
                return OperateResult.success(countActiveUsersForLast31Days());
            case MONTH:
                return OperateResult.success(countActiveUsersForLast12Months());
            default:
                return OperateResult.error("无效的类型: " + type);
        }
    }
}