// package com.ctdi.cnos.llm.base.service;
//
// import cn.hutool.core.util.ObjUtil;
// import com.ctdi.cnos.llm.context.UserContextHolder;
// import com.ctdi.cnos.llm.system.auth.AuthClient;
// import com.ctdi.cnos.llm.system.entity.UserVO;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Component;
// import org.springframework.web.context.request.RequestContextHolder;
// import org.springframework.web.context.request.ServletRequestAttributes;
//
// import javax.servlet.http.HttpServletRequest;
//
// /**
//  * 用户信息提取。
//  *
//  * @author laiqi
//  * @since 2024/7/4
//  */
// @Component
// @RequiredArgsConstructor
// public class UserData implements IUserData {
//
//     private final AuthClient authClient;
//
//     @Override
//     public Long getUserId() {
//         return UserContextHolder.getUserId();
//     }
//
//     @Override
//     public UserVO takeFromRequest() {
//         UserVO userVO = UserContextHolder.getUser();
//         if (ObjUtil.isNotNull(userVO)) {
//             return userVO;
//         }
//         HttpServletRequest request = getHttpRequest();
//         if (ObjUtil.isNotNull(request)) {
//             return getUserFromRequest(request);
//         }
//         return null;
//     }
//
//     /**
//      * 获取Servlet请求上下文的HttpRequest对象。
//      *
//      * @return 请求上下文中的HttpRequest对象。
//      */
//     public static HttpServletRequest getHttpRequest() {
//         ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//         return attributes == null ? null : attributes.getRequest();
//     }
//
//     @Override
//     public UserVO getUserFromRequest(HttpServletRequest request) {
//         String token = request.getHeader("Authorization");
//         return authClient.getUser(token);
//     }
// }