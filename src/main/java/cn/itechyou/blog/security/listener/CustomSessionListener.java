package cn.itechyou.blog.security.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import cn.itechyou.blog.security.session.ShiroSessionRepository;

/**
 * 
 * shiro 回话 监听
 * 
 */
public class CustomSessionListener implements SessionListener {

	private ShiroSessionRepository shiroSessionRepository;

	/**
	 * 一个回话的生命周期开始
	 */
	@Override
	public void onStart(Session session) {
		// System.out.println("on start");
	}

	/**
	 * 一个回话的生命周期结束
	 */
	@Override
	public void onStop(Session session) {
		// System.out.println("on stop");
	}

	@Override
	public void onExpiration(Session session) {
		shiroSessionRepository.deleteSession(session.getId());
	}

	public ShiroSessionRepository getShiroSessionRepository() {
		return shiroSessionRepository;
	}

	public void setShiroSessionRepository(ShiroSessionRepository shiroSessionRepository) {
		this.shiroSessionRepository = shiroSessionRepository;
	}

}
