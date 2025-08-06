package com.ctdi.cnos.llm.cache.aspect;

import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableAsync
public class AsyncCtgCacheService {
	@Autowired
	private CtgCacheSupport ctgCacheSupport;

	/**
	 * 根据缓存名称清空缓存
	 */
	@Async
	public void clearCache(CtgCache ctgCache, String cacheName) {
		log.info("异步清理缓存{}", cacheName);
		ctgCache.clear(cacheName);
	}

	@Async
	public void addClearTaskIfNotExists(String cacheName, CtgCache ctgCache) {
		ctgCacheSupport.addClearTaskIfNotExists(cacheName, ctgCache);
	}

}
