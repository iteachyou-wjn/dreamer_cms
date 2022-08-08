package cc.iteachyou.cms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.entity.User;
import cc.iteachyou.cms.exception.AdminGeneralException;
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

		String referer = request.getHeader("referer");
		if(referer != null && (referer.startsWith("http://cms.iteachyou.cc/") || referer.startsWith("https://cms.iteachyou.cc/"))) {
			if(handler instanceof HandlerMethod) {
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				Log annotation = handlerMethod.getMethodAnnotation(Log.class);
				if(annotation == null) {
					return Boolean.TRUE;
				}
				OperatorType operType = annotation.operType();
				if(!OperatorType.PAGE.equals(operType) && !OperatorType.SELECT.equals(operType) && !OperatorType.OTHER.equals(operType)) {
					throw new AdminGeneralException(ExceptionEnum.HTTP_FORBIDDEN.getCode(), ExceptionEnum.HTTP_FORBIDDEN.getMessage(), "演示环境不允许操作");
				}
			}
		}
		log.info("UserAuthorizationInterceptor：["+token+"]拦截通过...");
		return Boolean.TRUE;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
}
