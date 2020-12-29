package cn.itechyou.cms.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 栏目
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class Category {
    private String id;

    private String cnname;

    private String enname;

    private String code;
    
    private String catSeq;

    private String formId;

    private String imagePath;

    private String description;

    private String linkTarget;

    private Integer pageSize;

    private Integer catModel;

    private String visitUrl;

    private String coverTemp;

    private String listTemp;

    private String articleTemp;

    private String linkUrl;

    private String defaultEditor;

    private String parentId;
    
    private String parentName;

    private Integer isShow;

    private String level;
    
    private Integer sort;
    
    private Integer isInput;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String ext01;

    private String ext02;

    private String ext03;

    private String ext04;

    private String ext05;

    private List<CategoryWithBLOBs> nodes;
}