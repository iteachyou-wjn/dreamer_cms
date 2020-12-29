package cn.itechyou.cms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 主题
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class Theme {
	private String id;

	private String themeName;

	private String themeAuthor;

	private String themeImg;

	private Integer status;

	private String createBy;

	private Date createTime;

	private String updateBy;

	private Date updateTime;

	private String themePath;
}