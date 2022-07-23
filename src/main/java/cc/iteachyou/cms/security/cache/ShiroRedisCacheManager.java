package cc.iteachyou.cms.security.cache;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

@SuppressWarnings("rawtypes")
public class ShiroRedisCacheManager extends AbstractCacheManager {
	
	private RedisTemplate<byte[],byte[]> redisTemplate;

	public ShiroRedisCacheManager(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    
    //为了个性化配置redis存储时的key，我们选择了加前缀的方式，所以写了一个带名字及redis操作的构造函数的Cache类
	@Override
    protected Cache createCache(String name) throws CacheException {
        return new ShiroRedisCache(redisTemplate, name);
    }
	
}
