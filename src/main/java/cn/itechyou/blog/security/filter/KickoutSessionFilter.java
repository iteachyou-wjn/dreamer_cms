package cn.itechyou.blog.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.itechyou.blog.common.ResponseResult;
import cn.itechyou.blog.common.StateCodeEnum;
import cn.itechyou.blog.security.cache.VCache;
import cn.itechyou.blog.security.session.ShiroSessionRepository;
import cn.itechyou.blog.security.token.TokenManager;
import cn.itechyou.blog.utils.LoggerUtils;
/**
 * 相同帐号登录控制
 * 
 */
@SuppressWarnings({"unchecked","static-access"})
public class KickoutSessionFilter extends AccessControlFilter {
	//静态注入
	static String kickoutUrl;
	//在线用户
	final static String ONLINE_USER = KickoutSessionFilter.class.getCanonicalName()+ "_session_user";
	//踢出状态，true标示踢出
	final static String KICKOUT_STATUS = KickoutSessionFilter.class.getCanonicalName()+ "_kickout_status";
	static VCache cache;
	
	//session获取
	static ShiroSessionRepository shiroSessionRepository;
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		HttpServletRequest httpRequest = ((HttpServletRequest)request);
		String url = httpRequest.getRequestURI();
		Subject subject = getSubject(request, response);
		//如果是相关目录 or 如果没有登录 就直接return true
		if(url.startsWith("/open/") || (!subject.isAuthenticated() && !subject.isRemembered())){
			return Boolean.TRUE;
		}
		Session session = subject.getSession();
		Serializable sessionId = session.getId();
		/**
		 * 判断是否已经踢出
		 * 1.如果是Ajax 访问，那么给予json返回值提示。
		 * 2.如果是普通请求，直接跳转到登录页
		 */
		Boolean marker = (Boolean)session.getAttribute(KICKOUT_STATUS);
		if (null != marker && marker ) {
			Map<String, String> resultMap = new HashMap<String, String>();
			//判断是不是Ajax请求
			if (ShiroFilterUtils.isAjax(request) ) {
				LoggerUtils.getPlatformLogger().debug("当前用户已经在其他地方登录，并且是Ajax请求！");
				ResponseResult result = ResponseResult.Factory.newInstance(Boolean.FALSE, StateCodeEnum.USER_LOGIN_KICKOUT.getCode(), null, StateCodeEnum.USER_LOGIN_KICKOUT.getDescription());
				out(request, response,result);
			}
			return  Boolean.FALSE;
		}
		
		
		//从缓存获取用户-Session信息 <UserId,SessionId>
		LinkedHashMap<String, Serializable> infoMap = cache.get(ONLINE_USER, LinkedHashMap.class);
		//如果不存在，创建一个新的
		infoMap = null == infoMap ? new LinkedHashMap<String, Serializable>() : infoMap;
		
		//获取tokenId
		String userId = TokenManager.getUserId();
		
		//如果已经包含当前Session，并且是同一个用户，跳过。
		if(infoMap.containsKey(userId) && infoMap.containsValue(sessionId)){
			//更新存储到缓存1个小时（这个时间最好和session的有效期一致或者大于session的有效期）
			cache.setex(ONLINE_USER, infoMap, 3600);
			return Boolean.TRUE;
		}
		//如果用户相同，Session不相同，那么就要处理了
		/**
		 * 如果用户Id相同,Session不相同
		 * 1.获取到原来的session，并且标记为踢出。
		 * 2.继续走
		 */
		if(infoMap.containsKey(userId) && !infoMap.containsValue(sessionId)){
			Serializable oldSessionId = infoMap.get(userId);
			Session oldSession = shiroSessionRepository.getSession(oldSessionId);
			if(null != oldSession){
				//标记session已经踢出
				oldSession.setAttribute(KICKOUT_STATUS, Boolean.TRUE);
				shiroSessionRepository.saveSession(oldSession);//更新session
				LoggerUtils.getPlatformLogger().debug("kickout old session success,oldId[%s]",oldSessionId);
			}else{
				shiroSessionRepository.deleteSession(oldSessionId);
				infoMap.remove(userId);
				//存储到缓存1个小时（这个时间最好和session的有效期一致或者大于session的有效期）
				cache.setex(ONLINE_USER, infoMap, 3600);
			}
			return  Boolean.TRUE;
		}
		
		if(!infoMap.containsKey(userId) && !infoMap.containsValue(sessionId)){
			infoMap.put(userId, sessionId);
			//存储到缓存1个小时（这个时间最好和session的有效期一致或者大于session的有效期）
			cache.setex(ONLINE_USER, infoMap, 3600);
		}
		return Boolean.TRUE;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		//先退出
		Subject subject = getSubject(request, response);
		subject.logout();
		return false;
	}

	private void out(ServletRequest req, ServletResponse res,Object data) throws IOException{
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0L);
		response.setHeader("Pragma", "no-cache");			
		PrintWriter out = response.getWriter();
		out.write(JSONObject.toJSONString(data, SerializerFeature.WriteMapNullValue));
		response.flushBuffer();
	}

	public static void setShiroSessionRepository(
			ShiroSessionRepository shiroSessionRepository) {
		KickoutSessionFilter.shiroSessionRepository = shiroSessionRepository;
	}

	public static String getKickoutUrl() {
		return kickoutUrl;
	}

	public static void setKickoutUrl(String kickoutUrl) {
		KickoutSessionFilter.kickoutUrl = kickoutUrl;
	}

}
