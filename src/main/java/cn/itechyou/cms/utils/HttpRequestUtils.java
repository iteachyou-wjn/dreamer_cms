package cn.itechyou.cms.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * http访问方法实现
 * @author 王俊南 
 * @date: 2018-11-22
 */
public class HttpRequestUtils {
	private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class); // 日志记录

	public static HttpServletRequest getRequest(){
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attr == null ? null : attr.getRequest();
        return request;
	}
	
	/**
     * 获取请求IP地址
     *
     * @param request
     * @return
     */
    public static String getRequestAddr(HttpServletRequest request) {
        if (request == null) return "";
        String ip = request.getHeader("x-forwarded-for");
        if(StringUtil.isBlank(ip))
            ip = request.getHeader("x-real-ip");//兼容nginx反向代理
        if(StringUtil.isBlank(ip))
            ip = request.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip))
            ip = "本地主机";
        return ip;
    }
    
    /**
     * 获取浏览器型号
     *
     * @param request
     * @return
     */
    public static String getBrowser(HttpServletRequest request) {
        String browser = "未知";
        if (request != null) {
            try {
                String ua = getUserAgent(request);
                if (ua.contains("MSIE")) {//IE浏览器
                    int idx = ua.indexOf("MSIE");
                    browser = ua.substring(idx, ua.indexOf(";", idx));
                } else if (ua.contains("Edge/")) {//Microsoft Edge浏览器
                    int idx = ua.indexOf("Edge/");
                    browser = ua.substring(idx);
                } else if (ua.contains("gecko") && ua.contains("rv:11.0")) {
                    browser = "MSIE 11.0";
                } else if (ua.contains("OPR/")) {//Opera浏览器
                    int idx = ua.indexOf("OPR/");
                    browser = ua.substring(idx);
                    browser = browser.replace("OPR", "Opera");
                } else if (ua.contains("Chrome/")) {//Google Chrome浏览器
                    int idx = ua.indexOf("Chrome");
                    browser = ua.substring(idx, ua.indexOf(" ", idx));
                } else if (ua.contains("Firefox/")) {//Firefox浏览器
                    int idx = ua.indexOf("Firefox/");
                    browser = ua.substring(idx);
                } else if (ua.contains("Safari/")) {//Safari浏览器
                    int idx = ua.indexOf("Safari/");
                    browser = ua.substring(idx);
                }
            } catch (Exception e) {
                browser = "未知";
            }

        }
        return browser;
    }

    /**
     * 获取平台型号
     *
     * @param request
     * @return
     */
    public static String getPlatform(HttpServletRequest request) {
        String platform = "未知";
        if (request != null) {
            String ua = getUserAgent(request);
            if (ua.contains("Windows Phone")) {
                platform = "Windows Phone";
            } else if (ua.contains("Windows")) {
                platform = "Windows";
                if (ua.contains("Windows NT 10.0"))
                    platform = "Windows 10";
                else if (ua.contains("Windows NT 6.3"))
                    platform = "Windows 8.1";
                else if (ua.contains("Windows NT 6.2"))
                    platform = "Windows 8";
                else if (ua.contains("Windows NT 6.1"))
                    platform = "Windows 7";
                else if (ua.contains("Windows NT 6.0"))
                    platform = "Windows Vista";
                else if (ua.contains("Windows NT 5.1"))
                    platform = "Windows XP";
            } else if (ua.contains("iPad")) {
                platform = "iPad";
            } else if (ua.contains("iPhone")) {
                platform = "iPhone";
            } else if (ua.contains("Android")) {
                platform = "Android";
            } else if (ua.contains("Linux")) {
                platform = "Linux";
            }
        }
        return platform;
    }

    /**
     * 获取UserAgent
     *
     * @param request
     * @return
     */
    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = "";
        if (request != null) {
            userAgent = request.getHeader("user-agent");
        }
        return userAgent;
    }
	
}
