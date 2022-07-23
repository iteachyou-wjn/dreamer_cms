package cc.iteachyou.cms.security.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("rawtypes")
public class ShiroRedisCache<K,V> implements Cache<K,V> {
	private RedisTemplate redisTemplate;
    
    private String prefix = "dreamer-cms-shiro";

    public String getPrefix() {
        return prefix + ":";
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public ShiroRedisCache(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public ShiroRedisCache(RedisTemplate redisTemplate,String prefix){
        this(redisTemplate);
        this.prefix = prefix;
    }

    @Override
    public V get(K k) throws CacheException {
        if (k == null) {
            return null;
        }
        byte[] bytes = getBytesKey(k);
        return (V)redisTemplate.opsForValue().get(bytes);

    }

    @Override
    public V put(K k, V v) throws CacheException {
        if (k== null || v == null) {
            return null;
        }

        byte[] bytes = getBytesKey(k);
        redisTemplate.opsForValue().set(bytes, v);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        if(k==null){
            return null;
        }
        byte[] bytes =getBytesKey(k);
        V v = (V)redisTemplate.opsForValue().get(bytes);
        redisTemplate.delete(bytes);
        return v;
    }

    @Override
    public void clear() throws CacheException {
        redisTemplate.getConnectionFactory().getConnection().flushDb();

    }

    @Override
    public int size() {
        return redisTemplate.getConnectionFactory().getConnection().dbSize().intValue();
    }

    @Override
    public Set<K> keys() {
        byte[] bytes = (getPrefix()+"*").getBytes();
        Set<byte[]> keys = redisTemplate.keys(bytes);
        Set<K> sets = new HashSet<>();
        for (byte[] key:keys) {
            sets.add((K)key);
        }
        return sets;
    }

    @Override
    public Collection<V> values() {
        Set<K> keys = keys();
        List<V> values = new ArrayList<>(keys.size());
        for(K k :keys){
            values.add(get(k));
        }
        return values;
    }

    private byte[] getBytesKey(K key){
        if(key instanceof String){
            String prekey = this.getPrefix() + key;
            return prekey.getBytes();
        }else {
            return ObjectUtil.serialize(key);
        }
    }

}