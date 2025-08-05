package com.ctdi.cnos.llm.cache.aspect;

import com.ctdi.cnos.llm.cache.annotation.CtgCachePut;
import com.ctdi.cnos.llm.cache.annotation.CtgCacheable;
import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@EnableScheduling
public class CtgCacheSupport implements ApplicationContextAware, SmartInitializingSingleton, BeanPostProcessor {
	private ApplicationContext applicationContext;
	/**
	 * 缓存信息
	 */
	private static Map<String, String> cacheNameMap = new ConcurrentHashMap<>(64);
	/**
	 * 缓存名称对应的缓存实例
	 */
	private static Map<String, CtgCache> cacheInstMap = new ConcurrentHashMap<>(64);

	/**
	 * 缓存清理间隔
	 */
	private Long clearCacheExpireTime = 12 * 3600L;

	@Autowired
	private AsyncCtgCacheService asyncCtgCacheService;

	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Autowired
	private CtgCache ctgCache;

	private int min = 0;
	private int hour = 1;

	@Override
	public void afterSingletonsInstantiated() {
		if (log.isInfoEnabled()) {
			log.info("缓存注册信息：" + cacheNameMap);
		}
		if (cacheNameMap != null && cacheNameMap.size() > 0) {
			for (Map.Entry<String, String> entry : cacheNameMap.entrySet()) {
				String cacheName = entry.getKey();
				String cacheInstName = entry.getValue();
				if (ctgCache == null) {
					log.error("获取缓存操作对象实例为空:" + cacheInstName);
					continue;
				}
				cacheInstMap.put(cacheName, ctgCache);
				// 暂时取消定时清理缓存任务，集合元素移除方法框架未提供
				//addClearTask(cacheName, ctgCache);
			}
		}
		cacheNameMap.clear();
		cacheNameMap = null;
	}

	private void addClearTask(String cacheName, CtgCache ctgCache) {
		if (min >= 60) {
			min = 0;
			hour = hour + 1;
		}
		String expression = "0 " + min + " " + hour + " * * ?";
		CronTrigger cronTrigger = new CronTrigger(expression);
		min += 5;
		log.info("增加定时清理{}的set数据任务:{}", cacheName, expression);
		threadPoolTaskScheduler.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					String res = ctgCache.lock("clear_" + cacheName, "RUN", clearCacheExpireTime);
					if (!"OK".equals(res)) {
						log.info("{}已经在清理数据,本次自身退出", cacheName);
						return;
					}
					log.info("{}开始清理数据", cacheName);
					clearCacheSet(cacheName, ctgCache);
					log.info("{}完成清理数据", cacheName);
				} catch (Exception e) {
					log.error("定时清理cacheName的set数据失败", e);
				}
			}
		}, cronTrigger);
	}

	public void addClearTaskIfNotExists(String cacheName, CtgCache ctgCache) {
		if (cacheInstMap.containsKey(cacheName)) {
			return;
		}
		addClearTask(cacheName, ctgCache);
		cacheInstMap.put(cacheName, ctgCache);
	}

	public void clearCacheSet(String cacheName, CtgCache ctgCache) {
		/*long total = ctgCache.scard(cacheName);
		if (total == 0) {
			return;
		}*/
		Set<String> keySet = (Set<String>) ctgCache.smembers(cacheName);
		if (keySet == null || keySet.isEmpty()) {
			return;
		}
		for (String key : keySet) {
			if (!ctgCache.hasKey(cacheName, key)) {
				// todo 框架未提供移除set成员方法
				//ctgCache.srem(cacheName, key);
			}
		}
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Map<Method, CtgCacheable> annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
				new MethodIntrospector.MetadataLookup<CtgCacheable>() {
					@Override
					public CtgCacheable inspect(Method method) {
						return AnnotatedElementUtils.findMergedAnnotation(method, CtgCacheable.class);
					}
				});
		if (!annotatedMethods.isEmpty()) {
			annotatedMethods.forEach((method, ctgCacheable) -> processCacheInfo(ctgCacheable));
		}

		Map<Method, CtgCachePut> putAnnotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
				new MethodIntrospector.MetadataLookup<CtgCachePut>() {
					@Override
					public CtgCachePut inspect(Method method) {
						return AnnotatedElementUtils.findMergedAnnotation(method, CtgCachePut.class);
					}
				});
		if (!putAnnotatedMethods.isEmpty()) {
			putAnnotatedMethods.forEach((method, ctgCacheable) -> processCacheInfo(ctgCacheable));
		}
		return bean;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

	public void processCacheInfo(CtgCacheable ctgCacheable) {
		String cacheInstName = ctgCacheable.cacheInstName();
		String cacheName = ctgCacheable.cacheName();
		cacheNameMap.put(cacheName, cacheInstName);
	}

	public void processCacheInfo(CtgCachePut ctgCacheable) {
		String cacheInstName = ctgCacheable.cacheInstName();
		String cacheName = ctgCacheable.cacheName();
		cacheNameMap.put(cacheName, cacheInstName);
	}

	/**
	 * 根据缓存名称清空缓存
	 */
	public void clearCache(String cacheName) {
		CtgCache ctgCache = cacheInstMap.get(cacheName);
		if (ctgCache == null) {
			log.error("获取缓存操作对象实例为空:" + cacheName);
			return;
		}
		asyncCtgCacheService.clearCache(ctgCache, cacheName);
	}

	/**
	 * 根据缓存名称清空缓存
	 */
	public void clearCache(Set<String> cacheNameSet) {
		if (cacheNameSet != null && cacheNameSet.size() > 0) {
			cacheNameSet.forEach(cacheName -> clearCache(cacheName));
		}
	}

	/**
	 * 清空所有缓存
	 */
	public void clearCache() {
		clearCache(cacheInstMap.keySet());
	}
}
