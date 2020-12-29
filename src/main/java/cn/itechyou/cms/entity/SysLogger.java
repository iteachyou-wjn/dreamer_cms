package cn.itechyou.cms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 系统日志
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class SysLogger {
	public static final String UNKNOW = "Unknow";
	public static final String DEFAULT_CLASS = "LogService";
	
    private String id;

    private String level;

    private String operUser;

    private Integer operType;

    private String operSource;

    private String ip;

    private String module;

    private String browser;

    private String platform;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String extend1;

    private String extend2;

    private String extend3;

    private String content;
}