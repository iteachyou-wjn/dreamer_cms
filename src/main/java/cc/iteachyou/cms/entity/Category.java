package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

/**
 * 栏目
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
@Table(name = "system_category")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
    private String id;

	@Column(name = "cnname")
    private String cnname;

	@Column(name = "enname")
    private String enname;

	@Column(name = "code")
    private String code;
    
	@Column(name = "cat_seq")
    private String catSeq;

	@Column(name = "form_id")
    private String formId;

	@Column(name = "image_path")
    private String imagePath;

	@Column(name = "description")
    private String description;

	@Column(name = "link_target")
    private String linkTarget;

	@Column(name = "page_size")
    private Integer pageSize;

	@Column(name = "cat_model")
    private Integer catModel;

	@Column(name = "visit_url")
    private String visitUrl;

	@Column(name = "cover_temp")
    private String coverTemp;

	@Column(name = "list_temp")
    private String listTemp;

	@Column(name = "article_temp")
    private String articleTemp;

	@Column(name = "link_url")
    private String linkUrl;

	@Column(name = "default_editor")
    private String defaultEditor;

	@Column(name = "parent_id")
    private String parentId;

	@Column(name = "is_show")
    private Integer isShow;

	@Column(name = "level")
    private String level;
    
	@Column(name = "sort")
    private Integer sort;
    
	@Column(name = "is_input")
    private Integer isInput;

	@Column(name = "create_by")
    private String createBy;

	@Column(name = "create_time")
    private Date createTime;

	@Column(name = "update_by")
    private String updateBy;

	@Column(name = "update_time")
    private Date updateTime;

	@Column(name = "ext01")
    private String ext01;

	@Column(name = "ext02")
    private String ext02;

	@Column(name = "ext03")
    private String ext03;

	@Column(name = "ext04")
    private String ext04;

	@Column(name = "ext05")
    private String ext05;
	
	@Column(name = "md_content")
    private String mdContent;

	@Column(name = "html_content")
    private String htmlContent;
	
	@Transient
    private String parentName;

	@Transient
    private List<Category> nodes;
}