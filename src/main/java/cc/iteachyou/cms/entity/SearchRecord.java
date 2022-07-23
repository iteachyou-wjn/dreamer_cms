package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 搜索记录
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
@Table(name = "system_search")
public class SearchRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
    private String id;

	@Column(name = "keywords")
    private String keywords;

	@Column(name = "create_time")
    private Date createTime;
}