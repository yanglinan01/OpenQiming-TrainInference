package com.ctdi.cnos.llm.system.user.service;

import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.system.entity.UserLoginLog;
import com.ctdi.cnos.llm.system.user.entity.UserVO;

import java.util.Map;

/**
 * 用户登录日志 数据操作服务接口。
 *
 * @author laiqi
 * @since 2024/10/15
 */
public interface UserLoginLogService extends IBaseService<UserLoginLog, UserLoginLog> {

    /**
     * 用户当天是否登录过
     * @param userId 用户ID
     * @return true:已登录，false:未登录
     */
    boolean isUserLoginToday(Long userId);

    /**
     * 保存当前用户登录日志操作
     */
    void saveUserLoginLog(UserVO userVO);

    /**
     * 统计当前日期近31天每天的日活用户数
     *
     * @return 日期：活跃人数
     */
    Map<String, Long> countActiveUsersForLast31Days();

    /**
     * 统计当前日期前12个月每月的日活用户数
     *
     * @return 月份：活跃人数
     */
    Map<String, Long> countActiveUsersForLast12Months();

    OperateResult<Map<String, Long>> queryChart(StatType type);
}