package com.ctdi.cnos.llm.cache.annotation;

import com.ctdi.cnos.llm.cache.CacheConst;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Retention(RUNTIME)
@Target(METHOD)
public @interface CtgCacheCounter {
	/**
	 * 缓存名称
	 */
	String cacheName() default CacheConst.DEFAULT_CACHE_NAME;

	/**
	 * 缓存的 key，可以为空，如果指定要按照 SpEL 表达式编写，如果不指定，则缺省按照方法的所有参数进行组合
	 */
	String key() default "";

	long limit() default 1L;

	String condition() default "";

	/**
	 * 超时时间(单位秒)
	 */
	int expireTime() default 12 * 3600;
}
