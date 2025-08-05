package com.ctdi.cnos.llm.interceptor;


import com.alibaba.fastjson.JSON;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.response.ResultCode;
import com.ctdi.cnos.llm.system.auth.AuthClient;
import com.ctdi.cnos.llm.system.auth.AuthUtil;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements AsyncHandlerInterceptor {

    @Autowired(required = false)
    private AuthClient authClient;

    private static final String AUTH_HEADER_KEY = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "86400");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");

        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return false;
        }
        if (authClient == null) {
            return true;
        }
        Boolean isAuthIgnore = false;
        // 忽略带AuthIgnore注解的请求, 不做后续token认证校验
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AuthIgnore authIgnore = handlerMethod.getMethodAnnotation(AuthIgnore.class);
            if (authIgnore != null) {
                isAuthIgnore = true;
            }
        }


        // 获取请求头信息authorization信息
        final String authHeader = request.getHeader(AUTH_HEADER_KEY);
        if (StringUtils.isBlank(authHeader)) {
            if (Boolean.TRUE.equals(isAuthIgnore)) {
                return true;
            }
            this.setResponse(request, response, ResultCode.USER_NOT_LOGGED_IN);
            return false;
        }

        //获取用户信息
        OperateResult<UserVO> userInfo = authClient.getUserInfo(authHeader);
        if (userInfo.isSuccess()) {
            UserVO userVO = userInfo.getData();
            UserContextHolder.setUserId(userVO.getId());
            UserContextHolder.setUserName(userVO.getUserName());
            UserContextHolder.setUser(userVO);

            //判断是否不需要用户登录权限,或者是否有权限
            if (Boolean.TRUE.equals(isAuthIgnore) || AuthUtil.interceptorAuth(userVO)) {
                return true;
            } else {
                this.setResponse(request, response, ResultCode.PERMISSION_UNAUTHORISE);
                return false;
            }

        } else {
            if (Boolean.TRUE.equals(isAuthIgnore)) {
                return true;
            }
            this.setResponse(request, response, userInfo.getCode(), userInfo.getMessage());
            return false;
        }
    }

    public void setResponse(HttpServletRequest request,
                            HttpServletResponse response, ResultCode message) {
        setResponse(request, response, message.code(), message.message());
    }

    public void setResponse(HttpServletRequest request,
                            HttpServletResponse response, int code, String message) {

        response.setContentType("application/json;charset=UTF-8");
        try (Writer writer = response.getWriter()) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", code);
            resultMap.put("message", message);

            JSON.writeJSONString(writer, resultMap);
            writer.flush();
        } catch (IOException e) {
            log.error("response 设置操作异常：" + e);
        }
    }

    public void setResponse(HttpServletRequest request,
                            HttpServletResponse response, Integer messageKey) {
        setResponse(request, response, messageKey, "OK");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder.clear();
    }
}