package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 文章
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
@Table(name = "system_archives")
public class Archives implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id")
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "properties")
    private String properties;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "tag")
    private String tag;

    @Column(name = "description")
    private String description;

    @Column(name = "category_id")
    private String categoryId;
    
    @Column(name = "category_ids")
    private String categoryIds;

    @Column(name = "comment")
    private Integer comment;

    @Column(name = "subscribe")
    private Integer subscribe;

    @Column(name = "clicks")
    private Integer clicks;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_time")
    private Date updateTime;

}