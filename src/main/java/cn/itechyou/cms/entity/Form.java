package cn.itechyou.cms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 表单模型
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class Form {
    private String id;

    private String formName;

    private String tableName;

    private String code;

    private Integer type;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String ext01;

    private String ext02;

    private String ext03;

    private String ext04;

    private String ext05;
}