package cn.itechyou.cms.entity;

import java.io.Serializable;

public class System implements Serializable{
	
	private static final long serialVersionUID = 7066174006767441965L;

	private String id;

    private String website;

    private String title;

    private String keywords;

    private String describe;

    private String icp;

    private String copyright;

    private String uploaddir;
    
    private String staticdir;
    
    private Integer browseType;
    
    private String appid;
    
    private String appkey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website == null ? null : website.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe == null ? null : describe.trim();
    }

    public String getIcp() {
        return icp;
    }

    public void setIcp(String icp) {
        this.icp = icp == null ? null : icp.trim();
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright == null ? null : copyright.trim();
    }

    public String getUploaddir() {
        return uploaddir;
    }

    public void setUploaddir(String uploaddir) {
        this.uploaddir = uploaddir == null ? null : uploaddir.trim();
    }
    
	public String getStaticdir() {
		return staticdir;
	}

	public void setStaticdir(String staticdir) {
		this.staticdir = staticdir;
	}

	public Integer getBrowseType() {
		return browseType;
	}

	public void setBrowseType(Integer browseType) {
		this.browseType = browseType;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
}