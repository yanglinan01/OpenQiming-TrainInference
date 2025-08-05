/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.system.auth;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.BooleanUtil;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.system.user.entity.User;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.util.ModelUtil;

import java.util.Map;

/**
 * 权限工具
 *
 * @author huangjinhua
 * @since 2024/8/7
 */
public class AuthUtil {

    /**
     * 模拟用户
     */
    public static Map<String, UserVO> mockUser = MapUtil.newHashMap(true);

    static {
        mockUser.put("0", buildMockUser(0L, "system", "系统管理员", "集团", "911010000000000000000000", "32***600@JS", "1", "0", "0", "0"));
        mockUser.put("1", buildMockUser(1L, "zhangsan", "张三(区域领导)", "北京", "81101000xxxx00000000", "32***601@JS", "2", "1", "0", "0"));
        mockUser.put("2", buildMockUser(2L, "lisi", "李四", "南京", "91101000xxxx00000000", "32***602@JS", "3", "1", "0", "0"));
    }

    public static UserVO buildMockUser(Long id, String userName, String name, String regionName, String regionCode, String employeeNumber,
                                       String role, String systemAuth, String toolAuth, String agentAuth) {
        UserVO userVO = new UserVO();
        userVO.setId(id)
                .setUserName(userName)
                .setName(name)
                .setRegionName(regionName)
                .setRegionCode(regionCode)
                .setEmployeeNumber(employeeNumber)
                .setRole(role)
                .setSystemAuth(systemAuth)
                .setToolAuth(toolAuth)
                .setAgentAuth(agentAuth)
                .setIsAdmin(isAdmin(userVO))
                .setIsMock(true);
        return userVO;
    }

    /**
     * 拦截权限
     *
     * @param userVO 用户信息
     * @return user
     */
    public static boolean interceptorAuth(UserVO userVO) {
        if (userVO == null) {
            return false;
        }
        return BooleanUtil.isTrue(userVO.getIsAdmin()
                || SystemConstant.YES.equals(userVO.getSystemAuth())
                || SystemConstant.YES.equals(userVO.getToolAuth()))
                || SystemConstant.YES.equals(userVO.getAgentAuth());
    }

    /**
     * 判断是否是超级管理员
     *
     * @param user 用户
     * @return 布尔值
     */
    public static boolean isAdmin(User user) {
        if (user == null) {
            return false;
        }
        UserVO userVO = ModelUtil.copyTo(user, UserVO.class);
        return isAdmin(userVO);
    }

    /**
     * 判断是否是超级管理员
     *
     * @param userVO 用户
     * @return 布尔值
     */
    public static boolean isAdmin(UserVO userVO) {
        if (userVO == null) {
            return false;
        }
        return SystemConstant.USER_ADMIN_CODE.equals(userVO.getRole());
    }


    /**
     * 判断是否是超级管理员
     *
     * @param userVO 用户
     * @return 布尔值
     */
    public static boolean isMock(UserVO userVO) {
        if (userVO == null) {
            return false;
        }
        return userVO.getIsMock() != null && Boolean.TRUE.equals(userVO.getIsMock());
    }

    /**
     * 判断是否是有系统管理操作权限
     *
     * @param userVO 用户
     * @return 布尔值
     */
    public static boolean hasSystemManage(UserVO userVO) {
        if (userVO == null) {
            return false;
        }
        return isAdmin(userVO) || SystemConstant.YES.equals(userVO.getSystemAuth());
    }

    /**
     * 判断是否是有系统管理操作权限
     *
     * @param user 用户
     * @return 布尔值
     */
    public static boolean hasSystemManage(User user) {
        if (user == null) {
            return false;
        }
        UserVO userVO = ModelUtil.copyTo(user, UserVO.class);
        return hasSystemManage(userVO);
    }

    /**
     * 管理员赋予默认全权限
     *
     * @param user 用户信息
     */
    public static void adminAuth(User user) {
        if (user == null) {
            return;
        }
        // 超级管理员、系统管理员
        if (hasSystemManage(user)) {
            user.setSystemAuth(SystemConstant.YES);
            user.setToolAuth(SystemConstant.YES);
            user.setAgentAuth(SystemConstant.YES);
        }
    }

    /**
     * 默认全权限
     *
     * @param user 原始用户信息
     * @return user
     */
    public static User defaultAuth(User user) {
        if (user == null) {
            return null;
        }
        if (hasSystemManage(user)) {
            adminAuth(user);
        } else {
            //新增用户默认不开通工具链, 智能体权限 、系统管理权限
            if (CharSequenceUtil.isBlank(user.getAgentAuth())) {
                user.setAgentAuth(SystemConstant.NO);
            }
            if (CharSequenceUtil.isBlank(user.getToolAuth())) {
                user.setAgentAuth(SystemConstant.NO);
            }
            user.setSystemAuth(SystemConstant.NO);
            //普通用户权限
            user.setRole(SystemConstant.USER_ORDINARY_CODE);
        }
        return user;
    }

    /**
     * 菜单权限
     *
     * @param userVO 用户信息
     */
    public static void menuAuth(UserVO userVO) {
        if (userVO != null) {
            //0 无任何权限，1：工具链权限，2：智能体权限，3：工具链权限+智能体权限
            if (isAdmin(userVO)) {
                userVO.setMenuType("3");
            } else if (SystemConstant.YES.equals(userVO.getToolAuth()) && SystemConstant.YES.equals(userVO.getAgentAuth())) {
                userVO.setMenuType("3");
            } else if (SystemConstant.YES.equals(userVO.getToolAuth())) {
                userVO.setMenuType("1");
            } else if (SystemConstant.YES.equals(userVO.getAgentAuth())) {
                userVO.setMenuType("2");
            } else {
                userVO.setMenuType("0");
            }
        }
    }

    /**
     * 根据用户token值获取用户
     *
     * @param userVO 原始用户信息
     * @return userVO
     */
    public static UserVO copyUser(UserVO userVO) {
        if (userVO != null) {
            return ModelUtil.copyTo(userVO, UserVO.class);
        }
        return null;
    }
}