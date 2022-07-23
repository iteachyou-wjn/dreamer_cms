package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 表单模型字段
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
@Table(name = "system_fields")
public class Field implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
    private String id;

	@Column(name = "form_id")
    private String formId;

	@Column(name = "field_text")
    private String fieldText;

	@Column(name = "field_name")
    private String fieldName;

	@Column(name = "type")
    private Integer type;

	@Column(name = "data_type")
    private String dataType;

	@Column(name = "default_value")
    private String defaultValue;

	@Column(name = "max_length")
    private Integer maxLength;

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