package cn.itechyou.blog.security.cache;

import org.apache.shiro.cache.Cache;

/**
 * 
 * shiro cache manager 接口
 * 
 */
public interface ShiroCacheManager {

    <K, V> Cache<K, V> getCache(String name);

    void destroy();

}
