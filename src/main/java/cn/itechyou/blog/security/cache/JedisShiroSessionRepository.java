package cn.itechyou.blog.security.cache;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.session.Session;

import cn.itechyou.blog.security.session.CustomSessionManager;
import cn.itechyou.blog.security.session.SessionStatus;
import cn.itechyou.blog.security.session.ShiroSessionRepository;
import cn.itechyou.blog.utils.LoggerUtils;
import cn.itechyou.blog.utils.SerializeUtil;

/**
 * Session 管理
 * 
 * @author sojson.com
 *
 */
@SuppressWarnings("unchecked")
public class JedisShiroSessionRepository implements ShiroSessionRepository {
	public static final String REDIS_SHIRO_SESSION = "CMS-USER-SESSION:";
	// 这里有个小BUG，因为Redis使用序列化后，Key反序列化回来发现前面有一段乱码，解决的办法是存储缓存不序列化
	public static final String REDIS_SHIRO_ALL = "*CMS-USER-SESSION:*";
	private static final int SESSION_VAL_TIME_SPAN = 18000;
	private static final int DB_INDEX = 5;

	private JedisManager jedisManager;

	@Override
	public void saveSession(Session session) {
		if (session == null || session.getId() == null)
			throw new NullPointerException("session is empty");
		try {
			byte[] key = SerializeUtil.serialize(buildRedisSessionKey(session.getId()));

			// 不存在才添加。
			if (null == session.getAttribute(CustomSessionManager.SESSION_STATUS)) {
				// Session 踢出自存存储。
				SessionStatus sessionStatus = new SessionStatus();
				session.setAttribute(CustomSessionManager.SESSION_STATUS, sessionStatus);
			}

			byte[] value = SerializeUtil.serialize(session);
			/*
			 * 直接使用 (int) (session.getTimeout() / 1000) 的话，session失效和redis的TTL 同时生效
			 */
			getJedisManager().saveValueByKey(DB_INDEX, key, value, (int) (session.getTimeout() / 1000));
		} catch (Exception e) {
			LoggerUtils.getExceptionLogger().error("save session error，id:[%s]", session.getId(), e);
		}
	}

	@Override
	public void deleteSession(Serializable id) {
		if (id == null) {
			throw new NullPointerException("session id is empty");
		}
		try {
			getJedisManager().deleteByKey(DB_INDEX, SerializeUtil.serialize(buildRedisSessionKey(id)));
		} catch (Exception e) {
			LoggerUtils.getExceptionLogger().error("删除session出现异常，id:[%s]", id, e);
		}
	}

	@Override
	public Session getSession(Serializable id) {
		if (id == null)
			throw new NullPointerException("session id is empty");
		Session session = null;
		try {
			byte[] value = getJedisManager().getValueByKey(DB_INDEX, SerializeUtil.serialize(buildRedisSessionKey(id)));
			session = SerializeUtil.deserialize(value, Session.class);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.getExceptionLogger().error("获取session异常，id:[%s]", id, e);
		}
		return session;
	}

	@Override
	public Collection<Session> getAllSessions() {
		Collection<Session> sessions = null;
		try {
			sessions = getJedisManager().AllSession(DB_INDEX, REDIS_SHIRO_SESSION);
		} catch (Exception e) {
			LoggerUtils.getExceptionLogger().error("获取全部session异常", e);
		}

		return sessions;
	}

	private String buildRedisSessionKey(Serializable sessionId) {
		return REDIS_SHIRO_SESSION + sessionId;
	}

	public JedisManager getJedisManager() {
		return jedisManager;
	}

	public void setJedisManager(JedisManager jedisManager) {
		this.jedisManager = jedisManager;
	}
}
