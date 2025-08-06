package com.ctdi.cnos.llm.cache.annotation;



import com.ctdi.cnos.llm.cache.CacheConst;

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
public @interface CtgCacheEvict {
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
	 * allEntries是boolean类型，表示是否需要清除缓存中的所有元素。默认为false，表示不需要。当指定了allEntries为true时，Spring
	 * Cache将忽略指定的key
	 */
	boolean allEntries() default false;

	/**
	 * 清除操作默认是在对应方法成功执行之后触发的，即方法如果因为抛出异常而未能成功返回时也不会触发清除操作。使用beforeInvocation可以改变触发清除操作的时间，当我们指定该属性值为true时，Spring会在调用该方法之前清除缓存中的指定元素
	 */
	boolean beforeInvocation() default false;
}
