package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 系统日志
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
@Table(name = "system_logger")
public class SysLogger implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String UNKNOW = "Unknow";
	public static final String DEFAULT_CLASS = "LogService";
	
	@Id
	@Column(name = "id")
    private String id;
	
	@Column(name = "level")
    private String level;

	@Column(name = "oper_user")
    private String operUser;

	@Column(name = "oper_type")
    private String operType;

	@Column(name = "oper_source")
    private String operSource;

	@Column(name = "ip")
    private String ip;

	@Column(name = "module")
    private String module;

	@Column(name = "browser")
    private String browser;

	@Column(name = "platform")
    private String platform;

	@Column(name = "create_by")
    private String createBy;

	@Column(name = "create_time")
    private Date createTime;

	@Column(name = "update_by")
    private String updateBy;

	@Column(name = "update_time")
    private Date updateTime;

	@Column(name = "extend1")
    private String extend1;

	@Column(name = "extend2")
    private String extend2;

	@Column(name = "extend3")
    private String extend3;

	@Column(name = "content")
    private String content;
}