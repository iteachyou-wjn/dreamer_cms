package cn.itechyou.cms.security.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ShiroRedisCache<K, V> implements Cache<K, V> {
	private static final Logger logger = LoggerFactory.getLogger(ShiroRedisCache.class);
	
	private RedisTemplate<String,Object> redisTemplate;
	
	public ShiroRedisCache(RedisTemplate<String,Object> redisTemplate){
		this.redisTemplate = redisTemplate;
	}
	
	@Override
	public V get(K key) throws CacheException {
		V value = (V) this.redisTemplate.opsForValue().get(key.toString());
		return value;
	}

	@Override
	public V put(K key, V value) throws CacheException {
		this.redisTemplate.opsForValue().set(key.toString(), value);
		return value;
	}

	@Override
	public V remove(K key) throws CacheException {
		V val = this.get(key);
		this.redisTemplate.delete(key.toString());
		return val;
	}

	@Override
	public void clear() throws CacheException {
		this.redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
				conn.flushDb();
				return true;
			}
		});
		
	}

	@Override
	public int size() {
		return this.redisTemplate.execute(new RedisCallback<Integer>() {
			@Override
			public Integer doInRedis(RedisConnection conn) throws DataAccessException {
				return conn.keys("*".getBytes()).size();
			}
		});
	}

	@Override
	public Set<K> keys() {
		return this.redisTemplate.execute(new RedisCallback<Set<K>>() {
			@Override
			public Set<K> doInRedis(RedisConnection conn) throws DataAccessException {
				Set<K> set = new HashSet<K>();
				Set<byte[]> keys = conn.keys("*".getBytes());
				Iterator<byte[]> it = keys.iterator();
				while(it.hasNext()){
					set.add((K)it.next());
				}
				return set;
			}
		});
	}

	@Override
	public Collection<V> values() {
		return this.redisTemplate.execute(new RedisCallback<Set<V>>() {
			@Override
			public Set<V> doInRedis(RedisConnection conn) throws DataAccessException {
				Set<V> set = new HashSet<V>();
				Set<byte[]> keys = conn.keys("*".getBytes());
				Iterator<byte[]> it = keys.iterator();
				while(it.hasNext()){
					set.add((V)conn.get(it.next()));
				}
				return set;
			}
		});
	}

}
