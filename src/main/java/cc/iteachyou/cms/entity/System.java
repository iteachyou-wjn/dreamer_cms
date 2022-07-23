package cc.iteachyou.cms.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 系统配置
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
@Table(name = "system_setting")
public class System implements Serializable{
	
	private static final long serialVersionUID = 7066174006767441965L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "website")
    private String website;

	@Column(name = "title")
    private String title;

	@Column(name = "keywords")
    private String keywords;

	@Column(name = "`describe`")
    private String describe;

	@Column(name = "icp")
    private String icp;

	@Column(name = "copyright")
    private String copyright;

	@Column(name = "uploaddir")
    private String uploaddir;
    
	@Column(name = "staticdir")
    private String staticdir;
    
	@Column(name = "browse_type")
    private Integer browseType;
    
	@Column(name = "appid")
    private String appid;
    
	@Column(name = "appkey")
    private String appkey;
}