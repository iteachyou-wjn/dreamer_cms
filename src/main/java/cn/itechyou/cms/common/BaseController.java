package cn.itechyou.cms.common;

import java.io.Serializable;

/**
 * controller基类，实现将JSON格式结果的输出方法
 */
public class BaseController implements Serializable {
    
    private static final long serialVersionUID = 1;

    //    protected HttpServletRequest request;
    //    protected HttpServletResponse response;
    //    protected HttpSession session;

    //    @ModelAttribute
    //    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
    //        this.request = request;
    //        this.response = response;
    //        this.session = request.getSession();
    //    }

    //    /**
    //     * 从Session中取得用户信息
    //     * 
    //     * @return 用户信息
    //     */
    //    public Object getLoginUser() {
    //        HttpSession session = this.getRequest().getSession();
    //        Object obj = session.getAttribute(Constant.SESSION_USER);
    //        if (null == obj) {
    //            throw new RuntimeException("PlatSysUser为空,登陆出错!");
    //        }
    //        return obj;
    //    }

    /**
     * 取得HttpServletRequest对象
     * 
     * @return HttpServletRequest对象
     */
    //    public HttpServletRequest getRequest() {
    //        return request;
    //    }

    /**
     * 取得Response对象
     * 
     * @return
     */
    //    public HttpServletResponse getResponse() {
    //        return response;
    //    }

    /**
     * 转换字符串为json 格式
     * 
     * @param str
     *            普通字符串
     */
    //    public void outJsonString(String str) {
    //        getResponse().setContentType("text/html;charset=UTF-8");
    //        outString(str);
    //    }

    /**
     * 转换字符串为json 格式，并设置ContentType为text/html
     * 
     * @param str
     *            普通字符串
     */
    //    public void outJson(Object obj) {
    //        getResponse().setContentType("text/html;charset=UTF-8");
    //        outJsonString(JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
    //    }

    /**
     * 输出字符串到页面
     * 
     * @param str
     *            字符
     */
    //    public void outString(String str) {
    //        try {
    //            HttpServletResponse response = getResponse();
    //            response.setHeader("Cache-Control", "no-cache");
    //            response.setHeader("Cache-Control", "no-store");
    //            response.setDateHeader("Expires", 0L);
    //            response.setHeader("Pragma", "no-cache");
    //            response.getWriter().write(str);
    //            response.flushBuffer();
    //        }
    //        catch (IOException e) {}
    //    }

    /**
     * 输出xml文本串到页面
     * 
     * @param xmlStr
     *            xml串
     */
    //    public void outXMLString(String xmlStr) {
    //        getResponse().setContentType("application/xml;charset=UTF-8");
    //        outString(xmlStr);
    //    }

}
