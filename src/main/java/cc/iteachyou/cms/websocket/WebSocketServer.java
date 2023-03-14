package cc.iteachyou.cms.websocket;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cc.iteachyou.cms.common.Constant;
import cc.iteachyou.cms.common.StateCodeEnum;
import cc.iteachyou.cms.entity.Message;
import cc.iteachyou.cms.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * WebSocket
 * @author Wangjn
 *
 */
@Slf4j
@ServerEndpoint("/admin/ws/{clientId}")
@Component
public class WebSocketServer {
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
		if (Constant.POOL.containsKey(clientId)) {
			Constant.POOL.remove(clientId);
			Constant.POOL.put(clientId, this);// 加入set中
		} else {
			Constant.POOL.put(clientId, this);// 加入set中
			addOnlineCount();// 在线数加1
			
		}
		log.info("用户连接:" + clientId + ",当前在线人数为:" + getOnlineCount());
		try {
			Message message = new Message(StateCodeEnum.HTTP_SUCCESS.getCode(),"",0);
			sendMessage(JSONObject.toJSONString(message));
		} catch (IOException e) {
			log.error("用户:" + clientId + ",网络异常!!!!!!");
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		if (Constant.POOL.containsKey(clientId)) {
			Constant.POOL.remove(clientId);
			subOnlineCount();// 从set中删除
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
		if (StringUtil.isNotBlank(message)) {
			try {
				// 解析发送的报文
				JSONObject jsonObject = JSON.parseObject(message);
				jsonObject.put("fromUserId", this.clientId);// 追加发送人(防止串改)
				String toUserId = jsonObject.getString("toUserId");
				if (StringUtil.isNotBlank(toUserId) && Constant.POOL.containsKey(toUserId)) {// 传送给对应toUserId用户的websocket
					Constant.POOL.get(toUserId).sendMessage(jsonObject.toJSONString());
				} else {
					log.error("请求的userId:" + toUserId + "不在该服务器上");// 否则不在这个服务器上，发送到mysql或者redis
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
	}

	/**
	 * 实现服务器主动推送
	 */
	public void sendMessage(String message) throws IOException {
		synchronized(session) {
			this.session.getBasicRemote().sendText(message);
		}
	}
	
	/**
	 * 实现服务器主动推送
	 * @throws EncodeException 
	 */
	public void sendMessage(Object message) throws IOException, EncodeException {
		synchronized(session) {
			this.session.getBasicRemote().sendObject(message);
		}
	}

	/**
	 * 发送自定义消息
	 * @throws EncodeException 
	 */
	public static void sendInfo(Message message, @PathParam("userId") String userId) throws IOException, EncodeException {
		if (StringUtil.isNotBlank(userId) && Constant.POOL.containsKey(userId)) {
			Constant.POOL.get(userId).sendMessage(JSONObject.toJSONString(message));
		} else {
			log.error("用户" + userId + ",不在线！");
		}
	}

	public static synchronized int getOnlineCount() {
		return Constant.ONLINE;
	}

	public static synchronized void addOnlineCount() {
		Constant.ONLINE++;
	}

	public static synchronized void subOnlineCount() {
		Constant.ONLINE--;
	}
}
