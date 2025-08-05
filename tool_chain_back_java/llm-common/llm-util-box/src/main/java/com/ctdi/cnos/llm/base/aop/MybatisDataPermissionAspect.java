package com.ctdi.cnos.llm.base.aop;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.ctdi.cnos.llm.context.ContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据权限Mapper方法重名权限注解切面。
 *
 * @author laiqi
 * @since 2024/7/30
 */
@Slf4j
@Aspect
public class MybatisDataPermissionAspect {

    /**
     * 方法注解缓存。
     */
    private static Map<Method, InterceptorIgnore> METHOD_INTERCEPTOR_IGNORE_CACHE = new HashMap<>();

    @Around("within(@org.apache.ibatis.annotations.Mapper *)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取连接点签名
        Signature signature = joinPoint.getSignature();
        // 将其转换为方法签名
        MethodSignature methodSignature = (MethodSignature) signature;
        // 通过方法签名获取被调用的目标方法
        Method method = methodSignature.getMethod();
        InterceptorIgnore ignore = METHOD_INTERCEPTOR_IGNORE_CACHE.computeIfAbsent(method, m -> {
            InterceptorIgnore annotation = m.getAnnotation(InterceptorIgnore.class);
            METHOD_INTERCEPTOR_IGNORE_CACHE.put(m, annotation);
            return annotation;
        });
        ContextHolder.set(ignore);
        // 继续执行原方法
        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            ContextHolder.clear();
        }
        return result;
    }
}