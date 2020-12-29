package cn.itechyou.cms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 搜索记录
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class SearchRecord {
    private String id;

    private String keywords;

    private Date createTime;
}