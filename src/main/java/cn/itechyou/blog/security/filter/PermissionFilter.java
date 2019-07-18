package cn.itechyou.blog.security.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.itechyou.blog.common.ResponseResult;
import cn.itechyou.blog.common.StateCodeEnum;
import cn.itechyou.blog.utils.LoggerUtils;
/**
 * 
 * 权限校验 Filter
 * 
 */
public class PermissionFilter extends AccessControlFilter {
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		//先判断带参数的权限判断
		Subject subject = getSubject(request, response);
		if(null != mappedValue){
			String[] arra = (String[])mappedValue;
			for (String permission : arra) {
				if(subject.isPermitted(permission)){
					return Boolean.TRUE;
				}
			}
		}
		HttpServletRequest httpRequest = ((HttpServletRequest)request);
		/**
		 * 此处是改版后，为了兼容项目不需要部署到root下，也可以正常运行，但是权限没设置目前必须到root 的URI，
		 * 原因：如果你把这个项目叫 ShiroDemo，那么路径就是 /ShiroDemo/xxxx.shtml ，那另外一个人使用，又叫Shiro_Demo,那么就要这么控制/Shiro_Demo/xxxx.shtml 
		 * 理解了吗？
		 * 所以这里替换了一下，使用根目录开始的URI
		 */
		
		String uri = httpRequest.getRequestURI();//获取URI
		String basePath = httpRequest.getContextPath();//获取basePath
		if(null != uri && uri.startsWith(basePath)){
			uri = uri.replaceFirst(basePath, "");
		}
		
		if(subject.isPermitted(uri)){
			return Boolean.TRUE;
		}
		if(ShiroFilterUtils.isAjax(request)){
			if(!subject.isPermitted(uri)){
				return Boolean.FALSE;
			}
			LoggerUtils.getPlatformLogger().debug("当前用户没有登录，并且是Ajax请求！");
			ResponseResult result = ResponseResult.Factory.newInstance(false, StateCodeEnum.USER_USER_NOLOGIN.getCode(), null, StateCodeEnum.USER_USER_NOLOGIN.getDescription());
			ShiroFilterUtils.out(response, result);
		}
		return Boolean.FALSE;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
		if (null == subject.getPrincipal()) {// 表示没有登录，重定向到登录页面
			ResponseResult result = ResponseResult.Factory.newInstance(Boolean.FALSE, StateCodeEnum.USER_USER_NOLOGIN.getCode(), null, StateCodeEnum.USER_USER_NOLOGIN.getDescription());
			out(request,response,result);
			saveRequest(request);
			WebUtils.issueRedirect(request, response, ShiroFilterUtils.LOGIN_URL);
		} else {
			if (StringUtils.hasText(ShiroFilterUtils.UNAUTHORIZED)) {// 如果有未授权页面跳转过去
				ResponseResult result = ResponseResult.Factory.newInstance(Boolean.FALSE, StateCodeEnum.USER_USER_NOPERMISSION.getCode(), null, StateCodeEnum.USER_USER_NOPERMISSION.getDescription());
				out(request,response,result);
				//WebUtils.issueRedirect(request, response, ShiroFilterUtils.UNAUTHORIZED);
			} else {// 否则返回401未授权状态码
				ResponseResult result = ResponseResult.Factory.newInstance(Boolean.FALSE, StateCodeEnum.USER_USER_NOPERMISSION.getCode(), null, StateCodeEnum.USER_USER_NOPERMISSION.getDescription());
				out(request,response,result);
				//WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		return Boolean.FALSE;
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

}
