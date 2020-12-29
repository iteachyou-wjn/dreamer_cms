package cn.itechyou.cms.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 系统配置
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
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
}