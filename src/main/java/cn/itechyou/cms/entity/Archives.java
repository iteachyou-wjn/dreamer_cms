package cn.itechyou.cms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 文章
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class Archives {
    
    private String id;

    private String title;

    private String properties;

    private String imagePath;

    private String tag;

    private String description;

    private String categoryId;
    
    private String categoryIds;

    private Integer comment;

    private Integer subscribe;

    private Integer clicks;

    private Integer weight;

    private Integer status;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}