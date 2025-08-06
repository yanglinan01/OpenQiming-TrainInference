package com.ctdi.cnos.llm.cache.annotation;



import com.ctdi.cnos.llm.cache.CacheConst;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 在执行前不会去检查缓存中是否存在之前执行过的结果，而是每次都会执行该方法，并将执行结果以键值对的形式存入指定的缓存中
 * 
 * @author lw
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface CtgCachePut {
	/**
	 * 缓存实例名称
	 */
	String cacheInstName() default CacheConst.DEFAULT_CACHE_INST_NAME;

	/**
	 * 缓存名称
	 */
	String cacheName() default CacheConst.DEFAULT_CACHE_NAME;

	/**
	 * 缓存的 key，可以为空，如果指定要按照 SpEL 表达式编写，如果不指定，则缺省按照方法的所有参数进行组合
	 */
	String key() default "";

	/**
	 * 缓存的条件，可以为空，使用 SpEL 编写，返回 true 或者 false，只有为 true 才进行缓存
	 */
	String condition() default "";
	
	/**
	 * 超时时间(单位秒)
	 */
	int expireTime() default 12 * 3600;
}
