package cn.itechyou.blog.security.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import cn.itechyou.blog.common.ResponseResult;
import cn.itechyou.blog.common.StateCodeEnum;
import cn.itechyou.blog.entity.User;
import cn.itechyou.blog.security.token.TokenManager;
import cn.itechyou.blog.utils.LoggerUtils;
/**
 * 
 * @author zhou-baicheng
 * @email  so@sojson.com
 * @version 1.0,2016年6月2日 <br/>
 * 
 */
public class LoginFilter  extends AccessControlFilter {
	final static Class<LoginFilter> CLASS = LoginFilter.class;
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		User token = TokenManager.getToken();
		
		if(null != token || isLoginRequest(request, response)){// && isEnabled()
            return Boolean.TRUE;
        } 
		if (ShiroFilterUtils.isAjax(request)) {// ajax请求
			ResponseResult result = ResponseResult.Factory.newInstance(false, StateCodeEnum.USER_USER_NOLOGIN.getCode(), null, StateCodeEnum.USER_USER_NOLOGIN.getDescription());
			LoggerUtils.getPlatformLogger().debug("当前用户没有登录，并且是Ajax请求！");
			ShiroFilterUtils.out(response, result);
		}
		WebUtils.issueRedirect(request, response, ShiroFilterUtils.LOGIN_URL);
		return Boolean.FALSE;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
			throws Exception {
		//保存Request和Response 到登录后的链接
		saveRequestAndRedirectToLogin(request, response);
		return Boolean.FALSE ;
	}
	

}
