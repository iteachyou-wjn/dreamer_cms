package cn.itechyou.cms.security.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ShiroRedisCacheManager implements CacheManager {
	
	private RedisTemplate<String,Object> redisTemplate;
	
	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();
	
	@Override
	public Cache<Object, Object> getCache(String name) throws CacheException {
		Cache<Object,Object> cache = this.caches.get(name);
		if(cache == null){
			cache = new ShiroRedisCache<Object, Object>(this.redisTemplate);
			this.caches.put(name, cache);
		}
		return cache;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
}
