package com.ctdi.cnos.llm.cache.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 用来标注在需要清除缓存元素的方法
 * 
 * @author lw
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface CtgCacheClear {
	/**
	 * 缓存名称
	 */
	String[] cacheName() default {};

	/**
	 * 缓存的条件，可以为空，使用 SpEL 编写，返回 true 或者 false，只有为 true 才进行缓存
	 */
	String condition() default "";

	/**
	 * 所有缓存
	 */
	boolean allCache() default false;
}
