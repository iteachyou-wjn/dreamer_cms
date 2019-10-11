package cn.itechyou.cms.entity;

import java.util.Date;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName == null ? null : themeName.trim();
	}

	public String getThemeAuthor() {
		return themeAuthor;
	}

	public void setThemeAuthor(String themeAuthor) {
		this.themeAuthor = themeAuthor == null ? null : themeAuthor.trim();
	}

	public String getThemeImg() {
		return themeImg;
	}

	public void setThemeImg(String themeImg) {
		this.themeImg = themeImg == null ? null : themeImg.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getThemePath() {
		return themePath;
	}

	public void setThemePath(String themePath) {
		this.themePath = themePath;
	}

}