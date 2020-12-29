package cn.itechyou.cms.entity;

import java.io.Serializable;
import java.util.Date;

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
public class User implements Serializable {
    private String id;

    private String username;

    private String password;

    private String mobile;

    private String realname;

    private Integer status;

    private String pageStyle;
    
    private String salt;

    private String lastLoginIp;

    private Date lastLoginTime;

    private String portrait;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
    
    @JSONField(serialize=false)
    @Transient
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