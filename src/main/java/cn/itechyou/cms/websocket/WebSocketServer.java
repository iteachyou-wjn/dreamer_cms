package cn.itechyou.cms.websocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.itechyou.cms.common.StateCodeEnum;
import cn.itechyou.cms.entity.Message;
import cn.itechyou.cms.utils.LoggerUtils;
import cn.itechyou.cms.utils.StringUtil;

/**
 * WebSocket
 * @author Wangjn
 *
 */
@ServerEndpoint("/admin/ws/{clientId}")
@Component
public class WebSocketServer {

	static final Logger log = LoggerUtils.getLogger(WebSocketServer.class);
	/** 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。 */
	private static int onlineCount = 0;
	/** concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。 */
	private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<String, WebSocketServer>();
	/** 与某个客户端的连接会话，需要通过它来给客户端发送数据 */
	private Session session;
	/** 接收userId */
	private String clientId = "";

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("clientId") String clientId) {
		this.session = session;
		this.clientId = clientId;
		if (webSocketMap.containsKey(clientId)) {
			webSocketMap.remove(clientId);
			webSocketMap.put(clientId, this);
			// 加入set中
		} else {
			webSocketMap.put(clientId, this);
			// 加入set中
			addOnlineCount();
			// 在线数加1
		}
		log.info("用户连接:" + clientId + ",当前在线人数为:" + getOnlineCount());
		try {
			Message message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"",0);
			sendTextMessage(JSONObject.toJSONString(message));
		} catch (IOException e) {
			log.error("用户:" + clientId + ",网络异常!!!!!!");
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		if (webSocketMap.containsKey(clientId)) {
			webSocketMap.remove(clientId);
			// 从set中删除
			subOnlineCount();
		}
		log.info("用户退出:" + clientId + ",当前在线人数为:" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("用户消息:" + clientId + ",报文:" + message);
		// 可以群发消息
		// 消息保存到数据库、redis
		if (StringUtil.isNotBlank(message)) {
			try {
				// 解析发送的报文
				JSONObject jsonObject = JSON.parseObject(message);
				// 追加发送人(防止串改)
				jsonObject.put("fromUserId", this.clientId);
				String toUserId = jsonObject.getString("toUserId");
				// 传送给对应toUserId用户的websocket
				if (StringUtil.isNotBlank(toUserId) && webSocketMap.containsKey(toUserId)) {
					webSocketMap.get(toUserId).sendTextMessage(jsonObject.toJSONString());
				} else {
					log.error("请求的userId:" + toUserId + "不在该服务器上");
					// 否则不在这个服务器上，发送到mysql或者redis
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("用户错误:" + this.clientId + ",原因:" + error.getMessage());
		error.printStackTrace();
	}

	/**
	 * 实现服务器主动推送
	 */
	public void sendTextMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}
	
	/**
	 * 实现服务器主动推送
	 * @throws EncodeException 
	 */
	public void sendObjectMessage(Object message) throws IOException, EncodeException {
		this.session.getBasicRemote().sendObject(message);
	}

	/**
	 * 发送自定义消息
	 * @throws EncodeException 
	 */
	public static void sendInfo(Message message, @PathParam("userId") String userId) throws IOException, EncodeException {
		log.info("发送消息到:" + userId + "，报文:" + message);
		if (StringUtil.isNotBlank(userId) && webSocketMap.containsKey(userId)) {
			webSocketMap.get(userId).sendTextMessage(JSONObject.toJSONString(message));
		} else {
			log.error("用户" + userId + ",不在线！");
		}
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketServer.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocketServer.onlineCount--;
	}
}
