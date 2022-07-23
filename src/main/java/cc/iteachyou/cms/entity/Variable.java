package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 变量
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
@Table(name = "system_variable")
public class Variable implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
    private String id;

	@Column(name = "item_name")
    private String itemName;

	@Column(name = "info")
    private String info;

	@Column(name = "type")
    private String type;

	@Column(name = "value")
    private String value;

	@Column(name = "create_by")
    private String createBy;

	@Column(name = "create_time")
    private Date createTime;

	@Column(name = "update_by")
    private String updateBy;

	@Column(name = "update_time")
    private Date updateTime;

}