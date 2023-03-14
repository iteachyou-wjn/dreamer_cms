package cc.iteachyou.cms.common;

import java.util.concurrent.ConcurrentHashMap;

import cc.iteachyou.cms.websocket.WebSocketServer;

/**
 * @ClassName: Constant 
 * @Description: 全局变量类
 * @author LIGW
 * @date 2018年5月10日 上午9:19:08
 */
public class Constant {
	
	/**
	 * 项目名称
	 */
	public static String projecName = "梦想家CMS内容管理系统";
	
	public static final String SESSION_USER = "SESSION_USER";
	
	public static final String PAGE_NUM_KEY = "pageNum";
	
	public static final String PAGE_SIZE_KEY = "pageSize";
	
	public static final Integer PAGE_NUM_VALUE = 1;
	
	public static final Integer PAGE_SIZE_VALUE = 10;
	
	public static final String KAPTCHA = "KAPTCHA";
	
	public static final String ERROR = "error/exception";
	
	public static final String ADMIN_ROLE = "00000000";
	
	/**
	 * 上传访问前缀
	 */
	public static final String UPLOAD_PREFIX = "resources/";
	
	/**
	 * 静态化访问前缀
	 */
	public static final String STATIC_PREFIX = "htmls/";
	
	/** 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。 */
	public static int ONLINE = 0;
	/** concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。 */
	public static ConcurrentHashMap<String, WebSocketServer> POOL = new ConcurrentHashMap<String, WebSocketServer>();
}
