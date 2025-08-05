package com.ctdi.cnos.llm.annotation;

/**
 * 数据权限注解。
 * 可以使用在类上，也可以使用在方法上。
 * - 如果 Mapper类加上注解，表示 Mapper提供的方法以及自定义的方法都会被加上数据权限
 * - 如果 Mapper类的方法加在上注解，表示该方法会被加上数据权限
 * - 如果 Mapper类和其方法同时加上注解，优先级为：【类上 > 方法上】
 * - 如果不需要数据权限，可以不加注解，也可以使用 @DataScope(enabled = false)
 *
 * @author huangjinhua
 * @since 2024-07-23
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface DataScope {

    /**
     * 是否生效，默认true-生效
     */
    boolean enabled() default true;

    /**
     * 表别名
     */
    String tableAlias() default "";

    /**
     * 权限控制范围的字段名称
     */
    String filterColumns() default "creator_id";

    /**
     * 是否同区域权限控制，默认true-生效
     */
    boolean isRegion() default true;
}
