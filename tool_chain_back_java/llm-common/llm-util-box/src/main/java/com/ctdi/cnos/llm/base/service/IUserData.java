// package com.ctdi.cnos.llm.base.service;
//
// import com.ctdi.cnos.llm.system.entity.UserVO;
//
// import javax.servlet.http.HttpServletRequest;
//
// /**
//  * 用户信息数据。
//  *
//  * @author laiqi
//  * @since 2024/7/4
//  */
// public interface IUserData {
//
//
//     /**
//      * 获取用户ID。
//      * @return
//      */
//     Long getUserId();
//
//     /**
//      * 从Http Request对象中获取用户对象。
//      *
//      * @return 令牌对象。
//      */
//     UserVO takeFromRequest();
//
//     /**
//      * 从请求头中获取用户信息
//      *
//      * @param request
//      * @return
//      */
//     UserVO getUserFromRequest(HttpServletRequest request);
// }