package cn.itechyou.cms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 表单模型字段
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class Field {
    private String id;

    private String formId;

    private String fieldText;

    private String fieldName;

    private Integer type;

    private String dataType;

    private String defaultValue;

    private Integer maxLength;

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