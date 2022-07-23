package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 附件
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
@Table(name = "system_attachment")
public class Attachment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
    private String id;

	@Column(name = "code")
    private String code;

	@Column(name = "filename")
    private String filename;

	@Column(name = "filesize")
    private Integer filesize;

	@Column(name = "filetype")
    private String filetype;

	@Column(name = "filepath")
    private String filepath;

	@Column(name = "create_by")
    private String createBy;

	@Column(name = "create_time")
    private Date createTime;

	@Column(name = "update_by")
    private String updateBy;

	@Column(name = "update_time")
    private Date updateTime;
}