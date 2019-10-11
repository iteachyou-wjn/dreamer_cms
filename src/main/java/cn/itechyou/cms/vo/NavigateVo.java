package cn.itechyou.cms.vo;

import java.util.ArrayList;
import java.util.List;

public class NavigateVo {
	private String id;
	
	private String navName;

	private String navId;
	
	private String pageName;

	private String pageId;
	
	private String pageUrl;
	
	private String pageTemp;

	private String parentId;

	private Integer orderBy;
	
	private String text;
	
	private List<NavigateVo> nodes = new ArrayList<NavigateVo>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNavName() {
		return navName;
	}

	public void setNavName(String navName) {
		this.navName = navName;
	}

	public String getNavId() {
		return navId;
	}

	public void setNavId(String navId) {
		this.navId = navId;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
		this.text = pageName;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getPageTemp() {
		return pageTemp;
	}

	public void setPageTemp(String pageTemp) {
		this.pageTemp = pageTemp;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public List<NavigateVo> getNodes() {
		return nodes;
	}

	public void setNodes(List<NavigateVo> nodes) {
		this.nodes = nodes;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = this.pageName;
	}

}
