package cc.iteachyou.cms.security.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.util.StrUtil;
import cc.iteachyou.cms.utils.StringUtil;

public class XssAndSqlFilter implements Filter {
	private List<String> excludes = new ArrayList<String>();
	private String isIncludeRichText;

    @Override
    public void destroy() {
    	
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	HttpServletRequest req = (HttpServletRequest) request;
    	if("true".equalsIgnoreCase(isIncludeRichText) && isExcludeUrl(req)) {
    		chain.doFilter(request, response);
    		return;
    	}
        String method = "GET";
        String param = "";
        XssAndSqlHttpServletRequestWrapper xssRequest = null;
        if (request instanceof HttpServletRequest) {
            method = ((HttpServletRequest) request).getMethod();
            xssRequest = new XssAndSqlHttpServletRequestWrapper((HttpServletRequest) request);
        }
        if ("POST".equalsIgnoreCase(method)) {
            param = this.getBodyString(xssRequest.getReader());
            if(StringUtil.isNotBlank(param)){
                if(xssRequest.checkXSSAndSql(param)){
                	request.getRequestDispatcher("/exception").forward(xssRequest, response);
                    return;
                }
            }
        }
        if (xssRequest.checkParameter()) {
        	request.getRequestDispatcher("/exception").forward(xssRequest, response);
            return;
        }
        chain.doFilter(xssRequest, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	this.isIncludeRichText = filterConfig.getInitParameter("isIncludeRichText");
    	String tempExcludes = filterConfig.getInitParameter("excludes");
    	
    	if (StrUtil.isNotEmpty(tempExcludes)) {
            String[] url = tempExcludes.split(",");
            Collections.addAll(excludes, url);
        }
    }

    // 获取request请求body中参数
    public static String getBodyString(BufferedReader br) {
        String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return str;
    }
    
    /**
     * 判断当前路径是否需要过滤
     */
    private boolean isExcludeUrl(HttpServletRequest request) {
        if (excludes == null || excludes.isEmpty()) {
            return false;
        }
        String url = request.getServletPath();
        for (String pattern : excludes) {
            Pattern p = Pattern.compile("^" + pattern);
            Matcher m = p.matcher(url);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

}
