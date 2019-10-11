package cn.itechyou.cms.security.token;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

import cn.itechyou.cms.entity.User;
import cn.itechyou.cms.utils.SpringContextUtil;

/**
 * Shiro管理下的Token工具类
 */
public class TokenManager {
	//用户登录管理
	public static final DreamerCMSRealm realm = SpringContextUtil.getBean("dreamerCMSRealm", DreamerCMSRealm.class);
	/**
	 * 获取当前登录的用户User对象
	 * @return
	 */
	public static User getToken(){
		User user = (User)SecurityUtils.getSubject().getPrincipal();
		return user;
	}
	
	
	
	/**
	 * 获取当前用户的Session
	 * @return
	 */
	public static Session getSession(){
		return SecurityUtils.getSubject().getSession();
	}
	/**
	 * 获取当前用户NAME
	 * @return
	 */
	public static String getRealName(){
		return getToken().getRealname();
	}
	/**
	 * 获取当前用户ID
	 * @return
	 */
	public static String getUserId(){
		return getToken()==null?null:getToken().getId();
	}
	/**
	 * 把值放入到当前登录用户的Session里
	 * @param key
	 * @param value
	 */
	public static void setVal2Session(Object key ,Object value){
		getSession().setAttribute(key, value);
	}
	/**
	 * 从当前登录用户的Session里取值
	 * @param key
	 * @return
	 */
	public static Object getVal2Session(Object key){
		return getSession().getAttribute(key);
	}
	
	/**
	 * 登录
	 * @param user
	 * @param rememberMe
	 * @param salt 
	 * @return
	 */
	public static User login(User user,Boolean rememberMe, ByteSource salt){
		ShiroToken token = new ShiroToken(user.getUsername(), user.getPassword(), salt);
		token.setRememberMe(rememberMe);
		SecurityUtils.getSubject().login(token);
		return getToken();
	}


	/**
	 * 判断是否登录
	 * @return
	 */
	public static boolean isLogin() {
		return null != SecurityUtils.getSubject().getPrincipal();
	}
	/**
	 * 退出登录
	 */
	public static void logout() {
		SecurityUtils.getSubject().logout();
	}
	
	/**
	 * 清空当前用户权限信息。
	 * 目的：为了在判断权限的时候，再次会再次 <code>doGetAuthorizationInfo(...)  </code>方法。
	 * ps：	当然你可以手动调用  <code> doGetAuthorizationInfo(...)  </code>方法。
	 * 		这里只是说明下这个逻辑，当你清空了权限，<code> doGetAuthorizationInfo(...)  </code>就会被再次调用。
	 */
	public static void clearNowUserAuth(){
		realm.clearCachedAuthorizationInfo();
	}
	
}
