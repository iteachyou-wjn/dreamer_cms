package cn.itechyou.cms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.entity.User;
import cn.itechyou.cms.utils.LoggerUtils;

@Component
public class UserAuthorizationInterceptor implements HandlerInterceptor{
	private static final Logger logger = LoggerUtils.getPlatformLogger();
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		User token = TokenManager.getToken();
		if(token == null) {
			response.sendRedirect("/admin/u/toLogin");
			return Boolean.FALSE;
		}
		logger.info("UserAuthorizationInterceptor：["+token+"]拦截通过...");
		return Boolean.TRUE;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
}
