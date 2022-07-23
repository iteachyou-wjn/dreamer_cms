package cc.iteachyou.cms.vo;

import lombok.Data;

/**
 * 用户扩展实体
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class UserVO {
	private String username;
	private String password;
	private String vcode;
	private boolean rememberMe;
}
