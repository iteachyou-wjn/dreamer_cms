package cn.itechyou.cms.security.token;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.util.ByteSource;

/**
 * 
 * Shiro token
 * 
 */
public class ShiroToken extends UsernamePasswordToken implements java.io.Serializable {

	private static final long serialVersionUID = 1;

	public ShiroToken(String username, String password, ByteSource salt) {
		super(username, password);
		this.pswd = password;
		this.salt = salt;
	}

	/** 登录密码[字符串类型] 因为父类是char[] ] **/
	private String pswd;

	private ByteSource salt;

	public String getPswd() {
		return pswd;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	public ByteSource getSalt() {
		return salt;
	}

	public void setSalt(ByteSource salt) {
		this.salt = salt;
	}

}
