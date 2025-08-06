package com.ctdi.cnos.llm.base.annotation;

import java.lang.annotation.*;

/**
 * 主要用于标记数据权限中基于UserId进行过滤的字段。
 *
 * @author laiqi
 * @since 2024/7/18
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserFilterColumn {
}