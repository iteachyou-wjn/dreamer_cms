package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 文章标签
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
@Table(name = "system_labels")
public class Label implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
    private String id;

	@Column(name = "tag_name")
    private String tagName;

	@Column(name = "tag_count")
    private String tagCount;
    
	@Column(name = "pinyin")
    private String pinyin;
    
	@Column(name = "first_char")
    private String firstChar;

	@Column(name = "create_by")
    private String createBy;

	@Column(name = "create_time")
    private Date createTime;

	@Column(name = "update_by")
    private String updateBy;

	@Column(name = "update_time")
    private Date updateTime;
}