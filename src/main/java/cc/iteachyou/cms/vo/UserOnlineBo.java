package cc.iteachyou.cms.vo;

import java.io.Serializable;
import java.util.Date;

import cc.iteachyou.cms.entity.User;
import lombok.Data;

/**
 * Session + User Bo
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class UserOnlineBo extends User implements Serializable {

	private static final long serialVersionUID = 1L;

	// Session Id
	private String sessionId;
	// Session Host
	private String host;
	// Session创建时间
	private Date startTime;
	// Session最后交互时间
	private Date lastAccess;
	// Session timeout
	private long timeout;
	// session 是否踢出
	private boolean sessionStatus = Boolean.TRUE;

	public UserOnlineBo() {

	}

	public UserOnlineBo(User user) {
		super(user);
	}
}
