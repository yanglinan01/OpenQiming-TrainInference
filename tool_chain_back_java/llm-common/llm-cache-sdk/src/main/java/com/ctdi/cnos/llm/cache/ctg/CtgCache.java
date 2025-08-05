package com.ctdi.cnos.llm.cache.ctg;

import com.ctdi.cnos.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 集团组件方法封装
 */
@Slf4j
public class CtgCache {
	@Autowired
	private RedisService redisService;
	public static final String DIR_SEPARATOR = "/";

	public static final Long DEFAULT_EXPIRE = 12 * 3600L;

	public static String genCacheKey(String dir, String key) {
		String _dir = dir;
		if (_dir == null) {
			_dir = "";
		} else if (!_dir.endsWith(DIR_SEPARATOR)) {
			_dir += DIR_SEPARATOR;
		}
		return _dir + key;
	}

	public Object get(String cacheName, String key) {
		return redisService.get(genCacheKey(cacheName, key));
	}

	public <T> T getCache(String cacheName, String key) {
		return redisService.get(genCacheKey(cacheName, key));
	}

	/**
	 * 根据key删除存储的数据,返回成功删除数
	 */
	public Long del(String key) {
		boolean bool = redisService.deleteObject(key);
		if (bool) {
			return 1L;
		} else {
			return 0L;
		}
	}

	/**
	 * 将一个或多个值 values 插入到列表 key 的表头
	 */
	public void lpush(String key, List objectList) {
		redisService.setCacheList(key, objectList);
	}

	public void set(String cacheName, String key, Object value) {
		redisService.setCacheObject(genCacheKey(cacheName, key), value, DEFAULT_EXPIRE, TimeUnit.SECONDS);
	}

	public void set(String cacheName, String key, Object value, int timeInSeconds) {
		redisService.setCacheObject(genCacheKey(cacheName, key), value, Long.valueOf(timeInSeconds), TimeUnit.SECONDS);
	}

	public void remove(String cacheName, String key) {
		redisService.deleteObject(genCacheKey(cacheName, key));
	}

	public void clear(String cacheName) {
		Set<String> keySet = redisService.getCacheSet(cacheName);
		if (keySet == null || keySet.isEmpty()) {
			return;
		}
		for (Object key : keySet) {
			remove(cacheName, (String) key);
		}
		this.del(cacheName);
	}

	/**
	 * key存在不做任何动作，key不存在则设置值，设置成功返回OK
	 */
	public String lock(String key, String value, long expireTime) {
		return redisService.lock(key, value, expireTime);
	}

	/**
	 * 释放全局锁
	 */
	public void unlock(String lockName, String lockValue) {
		redisService.unlock(lockName, lockValue);
	}

	/**
	 * 将(域-值)对设置到哈希表 key 中
	 */
	public void hset(String key, String field, Object value) {
		redisService.setCacheMapValue(key, field, value);
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。当给定域不存在或是给定 key 不存在时返回null
	 */
	public Object hget(String key, String field) {
		return redisService.getCacheMapValue(key, field);
	}

	/**
	 * 返回哈希表 key 中，所有的域和值。当key不存在时返回空哈希表
	 */
	public Map<String, ? extends Object> hgetAll(String key) {
		return redisService.getCacheMap(key);
	}

	public String flushDb() {
		return redisService.flushDb();
	}

	/**
	 * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 end 指定。下标从0开始，-1表示最后一个元素
	 */
	public List<? extends Object> lrange(String key) {
		return redisService.getCacheList(key);
	}

	/**
	 * 以毫秒为单位设置 key 的生存时间
	 */
	public void pexpire(String key, long milliseconds) {
		redisService.expire(key, milliseconds);
	}

	/**
	 * 以秒为单位，返回给定 key的剩余生存时间
	 */
	public Long ttl(String key) {
		return redisService.getExpire(key);
	}

	public Collection<String> keys(String pattern) {
		return redisService.keys(pattern);
	}

	public boolean hasKey(String cacheName, String key) {
		String id = genCacheKey(cacheName, key);
		return redisService.hasKey(id);
	}

	/**
	 * 返回集合 key 中的所有成员
	 */
	public Set<? extends Object> smembers(String key) {
		return redisService.getCacheSet(key);
	}

	public Long incr(String key) {
		return redisService.getInc(key);
	}

	public Long decr(String key) {
		return redisService.getDecr(key);
	}
}
