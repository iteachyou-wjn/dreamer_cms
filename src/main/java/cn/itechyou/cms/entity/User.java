package cn.itechyou.cms.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.shiro.util.ByteSource;
import org.springframework.data.annotation.Transient;

import com.alibaba.fastjson.annotation.JSONField;

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

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPageStyle() {
        return pageStyle;
    }

    public void setPageStyle(String pageStyle) {
        this.pageStyle = pageStyle == null ? null : pageStyle.trim();
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp == null ? null : lastLoginIp.trim();
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait == null ? null : portrait.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public ByteSource getSaltByte() {
		return saltByte;
	}

	public void setSaltByte(ByteSource saltByte) {
		this.saltByte = saltByte;
	}
    
}