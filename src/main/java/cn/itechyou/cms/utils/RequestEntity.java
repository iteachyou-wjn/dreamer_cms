package cn.itechyou.cms.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * RequestUtils
 * @author Wangjn
 */
public class RequestEntity {
    //远程地址
    public String remoteAddr;
    //浏览器型号
    public String broswer;
    //平台型号
    public String platform;

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getBroswer() {
        return broswer;
    }

    public void setBroswer(String broswer) {
        this.broswer = broswer;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public RequestEntity(){

    }

    public RequestEntity(String remoteAddr, String broswer, String platform) {
        this.remoteAddr = remoteAddr;
        this.broswer = broswer;
        this.platform = platform;
    }

    /**
     * 获得HandlerRequest
     * @param request
     * @return
     */
    public static RequestEntity fromWebRequest(HttpServletRequest req){
    	RequestEntity request = new RequestEntity();
    	request.setRemoteAddr(HttpRequestUtils.getRequestAddr(req));
    	request.setBroswer(HttpRequestUtils.getBrowser(req));
    	request.setPlatform(HttpRequestUtils.getPlatform(req));
        return request;
    }
}
