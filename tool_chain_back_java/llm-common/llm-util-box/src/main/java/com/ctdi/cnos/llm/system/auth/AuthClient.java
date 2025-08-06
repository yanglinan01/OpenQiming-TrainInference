/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.system.auth;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.ctdi.cnos.llm.base.constant.CacheConstant;
import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import com.ctdi.cnos.llm.config.UserConfig;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.response.ResultCode;
import com.ctdi.cnos.llm.system.user.entity.ApiUser;
import com.ctdi.cnos.llm.system.user.entity.User;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.system.user.service.UserLoginLogService;
import com.ctdi.cnos.llm.system.user.service.UserService;
import com.ctdi.cnos.llm.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * 鉴权接口
 *
 * @author huangjinhua
 * @since 2024/7/10
 */
@Slf4j
@RequiredArgsConstructor
public class AuthClient {

    private final UserConfig userConfig;

    private final UserService userService;

    private final UserLoginLogService userLoginLogService;

    private final CtgCache ctgCache;


    /**
     * 根据用户token值获取用户信息
     *
     * @param token token
     * @return OperateResult<ApiUser>
     */
    public OperateResult<UserVO> getUserInfo(String token) {
        UserVO userVO = null;
        if (CharSequenceUtil.isBlank(token)) {
            log.error("当前token值为空！");
            return OperateResult.error(ResultCode.USER_NOT_LOGGED_IN);
        }
        //获取模拟用户
        userVO = AuthUtil.mockUser.get(token);
        if (userVO != null) {
            return OperateResult.success(userVO);
        }
        //获取缓存用户信息
        userVO = getCache(token);
        if (userVO != null) {
            return OperateResult.success(userVO);
        }

        //id 用作 token
        if (userConfig.isIdToToken() && NumberUtil.isLong(token)) {
            userVO = userService.queryById(Convert.toLong(token), true);
            this.handleExtra(userVO, true, token);
            if (userVO != null) {
                return OperateResult.success(userVO);
            }
        }

        //请求接口
        String responseBody = null;
        log.info("获取用户信息url：{}，token值：{}", userConfig.getLoginUrl(), token);
        try (HttpResponse response = HttpRequest.get(userConfig.getLoginUrl())
                .header("Authorization", token)
                .execute()) {
            responseBody = response.body();
            if (CharSequenceUtil.isBlank(responseBody)) {
                log.error("获取用户信息为空!");
                return OperateResult.error(ResultCode.PERMISSION_EXPIRE);
            }
            Integer code = Convert.toInt(JSONPath.eval(responseBody, "$.code"), null);
            if (code == 1000) {
                ApiUser apiUser = Convert.convert(ApiUser.class, JSONPath.eval(responseBody, "$.data"), null);
                log.info("用户信息：{}", JSON.toJSONString(apiUser));
                if (apiUser == null) {
                    return OperateResult.error(ResultCode.PERMISSION_EXPIRE);
                }
                userVO = this.handleApiUser(apiUser);
                //扩展属性，写缓存
                this.handleExtra(userVO, true, token);

                return OperateResult.success(userVO);
            } else if (code == 401) {
                return OperateResult.error(ResultCode.USER_NOT_LOGGED_IN);
            } else {
                String msg = Convert.toStr(JSONPath.eval(responseBody, "$.msg"), null);
                log.error("获取用户信息异常：{}", responseBody);
                return OperateResult.error(code, msg);
            }
        }
    }

    /**
     * 根据用户token值获取用户
     *
     * @param token token
     * @return ApiUser
     */
    public UserVO getUser(String token) {
        OperateResult<UserVO> userInfo = this.getUserInfo(token);
        if (userInfo.isSuccess()) {
            return userInfo.getData();
        }
        return null;
    }


    /**
     * 保存用户信息
     *
     * @param apiUser 接口返回的用户对象
     */
    private UserVO handleApiUser(ApiUser apiUser) {
        if (apiUser != null) {
            User user = ModelUtil.copyTo(apiUser, User.class);
            user.setUserName(apiUser.getUsername());
            //解析二级公司 中国电信集团-中国电信股份有限公司山东分公司-中国电信山东分公司本部
            if (CharSequenceUtil.isNotBlank(user.getCorpName())) {
                List<String> corpNameList = CharSequenceUtil.split(user.getCorpName(), "-");
                if (corpNameList.size() > 1) {
                    user.setGroupBranch(corpNameList.get(1));
                }
            }
            //解析用户归属
            if (CharSequenceUtil.isNotBlank(user.getEmployeeNumber())) {
                int index = user.getEmployeeNumber().indexOf("@");
                if (index > -1) {
                    String belong = CharSequenceUtil.sub(user.getEmployeeNumber(), index + 1, user.getEmployeeNumber().length());
                    user.setBelong(belong);
                }
            }
            user.setLastActiveTime(DateUtil.date());
            UserVO userVO = userService.queryByEmployeeNumber(user.getEmployeeNumber());
            if (userVO != null && userConfig.isLoginUpdate()) {
                //如果需要根据登录更新用户信息
                userService.updateByByEmployeeNumber(user);
            } else {
                AuthUtil.defaultAuth(user);
                userService.save(user);
            }
            //为了获取最新的数据信息
            return userService.queryById(user.getId(), true);
        }
        return null;
    }

    /**
     * 额外的用户信息，写缓存
     *
     * @param userVO     用户信息
     * @param isSetCache 是否写缓存
     * @param token      token 用作缓存key
     */
    private void handleExtra(UserVO userVO, boolean isSetCache, String token) {
        if (userVO != null) {
            //获取缓存用户信息
            UserVO cacheUserVO = getCache(token);
            if (cacheUserVO == null) {
                // 保存当前用户登录日志操作
                try {
                    userLoginLogService.saveUserLoginLog(userVO);
                } catch (Exception e) {
                    log.warn("保存当前用户登录日志操作异常！{}", e.getMessage(), e);
                }
            }
            userVO.setIsAdmin(AuthUtil.isAdmin(userVO));
            userVO.setIsMock(userVO.getIsMock() != null && userVO.getIsMock());
            if (isSetCache && CharSequenceUtil.isNotBlank(token)) {
                this.putCache(token, userVO);
            }
        }
    }

    /**
     * 删除用户缓存
     *
     * @param token token信息
     */
    public void removeCache(String token) {
        ctgCache.remove(CacheConstant.USER_CACHE_NAME, token);
    }

    /**
     * 添加用户缓存
     *
     * @param token token信息
     */
    private void putCache(String token, UserVO userVO) {
        ctgCache.set(CacheConstant.USER_CACHE_NAME, token, userVO, 3600);
    }

    /**
     * 添加用户缓存
     *
     * @param token token信息
     */
    private UserVO getCache(String token) {
        UserVO userVO = null;
        //获取缓存用户信息
        if (ctgCache.hasKey(CacheConstant.USER_CACHE_NAME, token)) {
            Object o = ctgCache.get(CacheConstant.USER_CACHE_NAME, token);
            if (o instanceof UserVO) {
                userVO = (UserVO) o;
            } else {
                userVO = JSON.parseObject(JSON.toJSONString(o), UserVO.class);
            }
            ctgCache.pexpire(token, 3600);
        }
        return userVO;
    }


}