package cn.itechyou.cms.security;


import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * 针对自定义的ShiroSession的Redis CRUD操作，通过isChanged标识符，确定是否需要调用Update方法
 * 通过配置securityManager在属性cacheManager查找从缓存中查找Session是否存在，如果找不到才调用下面方法
 * Shiro内部相应的组件（DefaultSecurityManager）会自动检测相应的对象（如Realm）是否实现了CacheManagerAware并自动注入相应的CacheManager。
 */
public class RedisCachingShiroSessionDao extends EnterpriseCacheSessionDAO {

    private static final Logger logger = LoggerFactory.getLogger(RedisCachingShiroSessionDao.class);

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    
    // 保存到Redis中key的前缀 prefix+sessionId
    public static String prefix = "";

    // 设置会话的过期时间
    private int seconds = 0;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
    
    /**
     * 如DefaultSessionManager在创建完session后会调用该方法；
     * 如保存到关系数据库/文件系统/NoSQL数据库；即可以实现会话的持久化；
     * 返回会话ID；主要此处返回的ID.equals(session.getId())；
     */
    @Override
    protected Serializable doCreate(Session session) {
        // 创建一个Id并设置给Session
        Serializable sessionId = super.doCreate(session);
        String key = this.prefix + sessionId.toString();
        this.redisTemplate.opsForValue().set(key, session, this.seconds);
        return sessionId;
    }

    /**
     * 根据会话ID获取会话
     *
     * @param sessionId 会话ID
     * @return ShiroSession
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        String key = prefix + sessionId;
        Session session = super.doReadSession(sessionId);
        if(session == null){
        	return (Session) this.redisTemplate.opsForValue().get(key);
        }
        return null;
    }

    /**
     * 更新会话；如更新会话最后访问时间/停止会话/设置超时时间/设置移除属性等会调用
     */
    @Override
    protected void doUpdate(Session session) {
    	super.doUpdate(session);
    	if(session != null){
    		String key = this.prefix + session.getId().toString();
            this.redisTemplate.opsForValue().set(key, session, this.seconds);
    	}
    }

    /**
     * 删除会话；当会话过期/会话停止（如用户退出时）会调用
     */
    @Override
    protected void doDelete(Session session) {
    	String key = this.prefix + session.getId().toString();
    	this.redisTemplate.delete(key);
    }

}