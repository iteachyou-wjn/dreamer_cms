package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.shiro.util.ByteSource;
import org.springframework.data.annotation.Transient;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 用户
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
@Table(name = "system_user")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "username")
    private String username;

	@Column(name = "password")
    private String password;

	@Column(name = "mobile")
    private String mobile;

	@Column(name = "realname")
    private String realname;

	@Column(name = "status")
    private Integer status;

	@Column(name = "page_style")
    private String pageStyle;
    
	@Column(name = "salt")
    private String salt;

	@Column(name = "last_login_ip")
    private String lastLoginIp;

	@Column(name = "last_login_time")
    private Date lastLoginTime;

	@Column(name = "portrait")
    private String portrait;

	@Column(name = "create_by")
    private String createBy;

	@Column(name = "create_time")
    private Date createTime;

	@Column(name = "update_by")
    private String updateBy;

	@Column(name = "update_time")
    private Date updateTime;
    
	@Transient
    @JSONField(serialize=false)
    private ByteSource saltByte;
    
    public User() {
		super();
	}

	public User(User user) {
		super();
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.mobile = user.getMobile();
		this.realname = user.getRealname();
		this.status = user.getStatus();
		this.pageStyle = user.getPageStyle();
		this.salt = user.getSalt();
		this.lastLoginIp = user.getLastLoginIp();
		this.lastLoginTime = user.getLastLoginTime();
		this.portrait = user.getPortrait();
		this.createBy = user.getCreateBy();
		this.createTime = user.getCreateTime();
		this.updateBy = user.getUpdateBy();
		this.updateTime = user.getUpdateTime();
	}
}