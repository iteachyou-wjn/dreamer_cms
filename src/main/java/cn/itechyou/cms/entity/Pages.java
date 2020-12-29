package cn.itechyou.cms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 页面片段
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class Pages {
    private String id;

    private String pageName;

    private String pageUrl;

    private String pageTemp;
    
    private String code;

    private Integer status;

    private String defaultEditor;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
}