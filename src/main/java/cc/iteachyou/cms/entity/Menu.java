package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import lombok.Data;

@Table(name = "system_menu")
@Data
public class Menu implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 菜单名称
     */
    @Column(name = "menu_name")
    private String menuName;

    /**
     * 菜单编码
     */
    @Column(name = "menu_code")
    private String menuCode;

    /**
     * 菜单路径
     */
    @Column(name = "menu_url")
    private String menuUrl;

    /**
     * 上级菜单
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 菜单图标
     */
    @Column(name = "menu_icon")
    private String menuIcon;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 描述
     */
    @Column(name = "remark")
    private String remark;
    
    @Transient
    private String parentMenuName;
    
    @Transient
    private Boolean checked = false;
    
    @Transient
    private List<Menu> children;

    private static final long serialVersionUID = 1L;
}