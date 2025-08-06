package com.ctdi.cnos.llm.system.user.dao;

import cn.hutool.core.date.DateTime;
import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.system.entity.UserLoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户登录日志 数据操作访问接口。
 *
 * @author laiqi
 * @since 2024/10/15
 */
@Mapper
public interface UserLoginLogDao extends BaseDaoMapper<UserLoginLog> {

    /**
     * 统计最近 31 天活跃用户数
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @return 最近 31 天活跃用户数
     */
    List<Map<String, Object>> countActiveUsersForLast31Days(@Param("startDate") DateTime startDate, @Param("endDate") DateTime endDate);

    /**
     * 统计最近 12 个月活跃用户数
     * @param startDate 起始日期
     * @param endDate 截止日期
     * @return 最近 12 个月活跃用户数
     */
    List<Map<String, Object>> countActiveUsersForLast12Months(@Param("startDate") DateTime startDate, @Param("endDate") DateTime endDate);
}