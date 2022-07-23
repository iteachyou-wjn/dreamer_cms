package cc.iteachyou.cms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cc.iteachyou.cms.entity.User;
import cc.iteachyou.cms.security.token.TokenManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserAuthorizationInterceptor implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		User token = TokenManager.getToken();
		if(token == null) {
			response.sendRedirect("/admin/u/toLogin");
			return Boolean.FALSE;
		}
		log.info("UserAuthorizationInterceptor：["+token+"]拦截通过...");
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
