package com.ctdi.cnos.llm.cache.aspect;

import com.ctdi.cnos.common.core.utils.StringUtils;
import com.ctdi.cnos.llm.cache.annotation.CtgCacheable;
import com.ctdi.cnos.llm.cache.annotation.CtgCacheClear;
import com.ctdi.cnos.llm.cache.annotation.CtgCacheCounter;
import com.ctdi.cnos.llm.cache.annotation.CtgCacheEvict;
import com.ctdi.cnos.llm.cache.annotation.CtgCachePut;
import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import com.ctdi.cnos.llm.cache.utils.CtgUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;

/**
 * 集团缓存注解切面
 *
 * @author lw
 *
 */

@Aspect
@Slf4j
public class CtgCacheAspect implements SmartInitializingSingleton, EnvironmentAware {
	public static final String DIR_SEPARATOR = "/";
	private ExpressionEvaluator evaluator = new ExpressionEvaluator();
	private final KeyGenerator keyGenerator = new SimpleKeyGenerator();
	@Autowired
	private CtgCacheSupport ctgCacheSupport;
	private Environment environment;
	private Boolean cacheEnable = null;

	@Autowired
	private CtgCache ctgCache;

	/**
	 * CtgCacheable 切面处理
	 */
	@Around(value = "@annotation(ctgCacheable)")
	public Object dealCtgCacheable(ProceedingJoinPoint point, CtgCacheable ctgCacheable) throws Throwable {
		if (Boolean.FALSE.equals(cacheEnable)) {
			return point.proceed();
		}
		Method method = getMethod(point);
		// 所有参数
		Object[] args = point.getArgs();
		boolean isConditionPassing = isConditionPassing(ctgCacheable.condition(), point.getTarget(), method, args);
		if (!isConditionPassing) {
			return point.proceed();
		}
		Object key = parseKey(ctgCacheable.key(), point.getTarget(), method, args);
		if (key == null) {
			return point.proceed();
		}
		String cacheName = ctgCacheable.cacheName();
		if (ctgCache == null) {
			log.warn("自定义缓存注解实例不存在");
			return point.proceed();
		}
		String cacheKey = CtgUtils.getStrFromObj(key);
		Object value = null;
		try {
			value = ctgCache.get(cacheName, cacheKey);
			if (value != null) {
				return value;
			}
		} catch (Exception e) {
			log.error("获取缓存信息出错:", e);
		}
		value = point.proceed();
		if (value != null) {
			try {
				int expireTime = ctgCacheable.expireTime();
				ctgCache.set(cacheName, cacheKey,value, expireTime);
			} catch (Exception e) {
				log.error("设置缓存信息出错:", e);
			}
		}
		return value;
	}

	/**
	 * CtgCachePut 切面处理
	 */
	@Around(value = "@annotation(ctgCachePut)")
	public Object dealCtgCachePut(ProceedingJoinPoint point, CtgCachePut ctgCachePut) throws Throwable {
		if (Boolean.FALSE.equals(cacheEnable)) {
			return point.proceed();
		}
		Method method = getMethod(point);
		// 所有参数
		Object[] args = point.getArgs();
		boolean isConditionPassing = isConditionPassing(ctgCachePut.condition(), point.getTarget(), method, args);
		if (!isConditionPassing) {
			return point.proceed();
		}
		Object key = parseKey(ctgCachePut.key(), point.getTarget(), method, args);
		if (key == null) {
			return point.proceed();
		}
		String cacheName = ctgCachePut.cacheName();
		if (ctgCache == null) {
			log.warn("自定义缓存注解实例不存在");
			return point.proceed();
		}

		String cacheKey = CtgUtils.getStrFromObj(key);
		Object value = point.proceed();
		if (value != null) {
			try {
				int expireTime = ctgCachePut.expireTime();
				ctgCache.set(cacheName, cacheKey, value, expireTime);
			} catch (Exception e) {
				log.error("设置缓存信息出错:", e);
			}
		}
		return value;
	}

	/**
	 * CtgCacheEvict 切面处理
	 */
	@Around(value = "@annotation(ctgCacheEvict)")
	public Object dealCtgCacheEvict(ProceedingJoinPoint point, CtgCacheEvict ctgCacheEvict) throws Throwable {
		if (Boolean.FALSE.equals(cacheEnable)) {
			return point.proceed();
		}
		Method method = getMethod(point);
		// 所有参数
		Object[] args = point.getArgs();
		if (ctgCache == null) {
			log.warn("自定义缓存注解实例不存在");
			return point.proceed();
		}
		boolean beforeInvocation = ctgCacheEvict.beforeInvocation();
		if (beforeInvocation) {
			dealCtgCacheEvict(ctgCacheEvict, ctgCache, point, method, args);
		}
		Object value = point.proceed();
		if (!beforeInvocation) {
			dealCtgCacheEvict(ctgCacheEvict, ctgCache, point, method, args);
		}
		return value;
	}

	/**
	 * CtgCacheClear 切面处理
	 */
	@Around(value = "@annotation(ctgCacheClear)")
	public Object dealCtgCacheClear(ProceedingJoinPoint point, CtgCacheClear ctgCacheClear) throws Throwable {
		if (Boolean.FALSE.equals(cacheEnable)) {
			return point.proceed();
		}
		Object value = point.proceed();
		try {
			boolean allCache = ctgCacheClear.allCache();
			if (allCache) {
				ctgCacheSupport.clearCache();
			} else {
				String[] cacheName = ctgCacheClear.cacheName();
				if (cacheName == null || cacheName.length == 0) {
					ctgCacheSupport.clearCache();
				}
				for (String name : cacheName) {
					ctgCacheSupport.clearCache(name);
				}
			}
		} catch (Exception e) {
			log.error("清空缓存信息出错:", e);
		}

		return value;
	}

	/**
	 * CtgCacheCounter 切面处理
	 */
	@Before(value = "@annotation(ctgCacheCounter)")
	public void dealCtgCacheCounterBefore(CtgCacheCounter ctgCacheCounter) throws Throwable {
		String id = CtgCache.genCacheKey(ctgCacheCounter.cacheName(), ctgCacheCounter.key());
		// 使用Redis的INCR命令原子地增加计数器的值，并返回增加后的值
		Long current = ctgCache.incr(id);

		// 检查计数器的值是否超过了阈值
		if (current > ctgCacheCounter.limit()) {
			// 如果超过阈值，则拒绝请求，并尝试将计数器的值减一（因为上一步已经加了一）
			// 注意：这里减一操作可能失败，因为其他请求可能同时也在操作这个计数器
			// 但这并不影响限流的效果，因为即使减一失败，计数器的值也仍然超过了阈值
			ctgCache.decr(id);
		} else {
			// 设置计数器的过期时间，实现滑动时间窗口的效果
			ctgCache.pexpire(id, ctgCacheCounter.expireTime());
		}
	}

	@After(value = "@annotation(ctgCacheCounter)")
	public void dealCtgCacheCounterAfter(CtgCacheCounter ctgCacheCounter) throws Throwable {
		ctgCache.decr(CtgCache.genCacheKey(ctgCacheCounter.cacheName(), ctgCacheCounter.key()));
	}

	private void dealCtgCacheEvict(CtgCacheEvict ctgCacheEvict, CtgCache cache, ProceedingJoinPoint point,
			Method method, Object... args) {
		String cacheName = ctgCacheEvict.cacheName();
		boolean allEntries = ctgCacheEvict.allEntries();
		if (allEntries) {
			cache.clear(cacheName);
			return;
		}
		boolean isConditionPassing = isConditionPassing(ctgCacheEvict.condition(), point.getTarget(), method, args);
		if (!isConditionPassing) {
			return;
		}
		Object key = parseKey(ctgCacheEvict.key(), point.getTarget(), method, args);
		if (key == null) {
			return;
		}
		try {
			String cacheKey = CtgUtils.getStrFromObj(key);
			cache.remove(cacheName, cacheKey);
		} catch (Exception e) {
			log.error("清空缓存信息出错:", e);
		}
	}

	private Method getMethod(JoinPoint joinPoint) throws Exception {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		return method;
	}

	/**
	 * 获取redis的key
	 */
	private Object parseKey(String keyExpression, Object target, Method method, Object... args) {
		try {
			if (StringUtils.isEmpty(keyExpression)) {
				return keyGenerator.generate(target, method, args);
			}
			EvaluationContext evaluationContext = evaluator.createEvaluationContext(target, target.getClass(), method,
					args);
			AnnotatedElementKey methodKey = new AnnotatedElementKey(method, target.getClass());
			return evaluator.key(keyExpression, methodKey, evaluationContext);
		} catch (Exception e) {
			log.error("获取缓存key异常：" + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 条件是否满足
	 */
	private boolean isConditionPassing(String conditionExpression, Object target, Method method, Object... args) {
		if (StringUtils.isEmpty(conditionExpression)) {
			return true;
		}
		try {
			EvaluationContext evaluationContext = evaluator.createEvaluationContext(target, target.getClass(), method,
					args);
			AnnotatedElementKey methodKey = new AnnotatedElementKey(method, target.getClass());
			return evaluator.condition(conditionExpression, methodKey, evaluationContext);
		} catch (Exception e) {
			log.error("判断缓存是否满足条件异常：" + e.getMessage(), e);
			return false;
		}
	}

	@Override
	public void afterSingletonsInstantiated() {
		String enable = environment.getProperty("cnos.common.ctg.cache.annotation.enabled");
		if (StringUtils.isEmpty(enable)) {
			enable = "true";
		}
		cacheEnable = Boolean.parseBoolean(enable);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = (ConfigurableEnvironment) environment;

	}

}
