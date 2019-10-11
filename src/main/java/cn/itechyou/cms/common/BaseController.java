package cn.itechyou.cms.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 *
 * controller基类，实现将JSON格式结果的输出方法
 */
@SuppressWarnings("unchecked")
public class BaseController implements Serializable {
	private static final long serialVersionUID = -1206237575319606156L;
	
	protected HttpServletRequest request;  
    protected HttpServletResponse response;  
    protected HttpSession session;

    @ModelAttribute  
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){  
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }  
	
	
	
	/**
	 * 从Session中取得用户信息
	 * @return 用户信息
	 */
	public Object getLoginUser() {
		HttpSession session = this.getRequest().getSession();
		Object obj = session.getAttribute(Constant.SESSION_USER);
		if(null == obj){
			throw new RuntimeException("PlatSysUser为空,登陆出错!");
		}
		return obj;
	}
	
	/**
	 * 取得HttpServletRequest对象
	 * @return HttpServletRequest对象
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * 取得Response对象
	 * @return
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * 转换字符串为json 格式
	 * @param str 普通字符串
	 */
	public void outJsonString(String str) {
		getResponse().setContentType("text/html;charset=UTF-8");
		outString(str);
	}
	
	/**
	 * 转换字符串为json 格式，并设置ContentType为text/html
	 * @param str 普通字符串
	 */
	public void outJson(Object obj) {
		getResponse().setContentType("text/html;charset=UTF-8");
		outJsonString(JSONObject.toJSONString(obj,SerializerFeature.WriteMapNullValue));
	}

	/**
	 * 输出字符串到页面
	 * @param str 字符
	 */
	public void outString(String str) {
		try {
			getResponse().setHeader("Cache-Control", "no-cache");
			getResponse().setHeader("Cache-Control", "no-store");
			getResponse().setDateHeader("Expires", 0L);
			getResponse().setHeader("Pragma", "no-cache");			
			PrintWriter out = getResponse().getWriter();
			out.write(str);
			getResponse().flushBuffer();
		} catch (IOException e) {
		}
	}

	/**
	 * 输出xml文本串到页面
	 * @param xmlStr xml串
	 */
	public void outXMLString(String xmlStr) {
		getResponse().setContentType("application/xml;charset=UTF-8");
		outString(xmlStr);
	}

}
