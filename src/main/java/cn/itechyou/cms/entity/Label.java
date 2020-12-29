package cn.itechyou.cms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 文章标签
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class Label {
    private String id;

    private String tagName;

    private String tagCount;
    
    private String pinyin;
    
    private String firstChar;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
}