package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 表单模型
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
@Table(name = "system_forms")
public class Form implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
    private String id;

	@Column(name = "form_name")
    private String formName;

	@Column(name = "table_name")
    private String tableName;

	@Column(name = "code")
    private String code;

	@Column(name = "type")
    private Integer type;

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
}