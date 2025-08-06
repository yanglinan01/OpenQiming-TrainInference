//package com.ctdi.llmtc.xtp.traininfer.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//@WebFilter(filterName = "tokenAuthFilter", urlPatterns = "/*")
//public class TokenAuthFilter implements Filter {
//    Logger logger = LoggerFactory.getLogger(TokenAuthFilter.class);
//    private static final String TOKEN = "token";
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        logger.info("do filter token auth filter, url is " + httpServletRequest.getRequestURI());
//        String token = httpServletRequest.getHeader(TOKEN);
//        if (token == null || token.isEmpty()) {
//            logger.warn("token is null or empty");
////            return;
//        }
//        logger.info("token is {}", token);
//        //todo token解析及校验
//        chain.doFilter(request, response);
//    }
//}
