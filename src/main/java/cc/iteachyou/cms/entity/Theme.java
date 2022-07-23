package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 主题
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
@Table(name = "system_theme")
public class Theme implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "theme_name")
	private String themeName;

	@Column(name = "theme_author")
	private String themeAuthor;

	@Column(name = "theme_img")
	private String themeImg;

	@Column(name = "theme_path")
	private String themePath;

	@Column(name = "status")
	private Integer status;

	@Column(name = "create_by")
	private String createBy;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "update_by")
	private String updateBy;

	@Column(name = "update_time")
	private Date updateTime;
}