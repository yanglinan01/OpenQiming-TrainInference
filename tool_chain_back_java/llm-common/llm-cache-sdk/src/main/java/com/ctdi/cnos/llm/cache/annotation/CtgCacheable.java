package com.ctdi.cnos.llm.cache.annotation;



import com.ctdi.cnos.llm.cache.CacheConst;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 每次执行前都会检查Cache中是否存在相同key的缓存元素，如果存在就不再执行该方法，而是直接从缓存中获取结果进行返回，否则才会执行并将返回结果存入指定的缓存中
 * 
 * @author lw
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface CtgCacheable {
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
