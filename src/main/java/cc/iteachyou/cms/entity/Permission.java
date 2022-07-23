package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Table(name = "system_permission")
@Data
public class Permission implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 权限名称
     */
    @Column(name = "permission_name")
    private String permissionName;

    /**
     * 权限编码
     */
    @Column(name = "permission_code")
    private String permissionCode;

    /**
     * 所属菜单
     */
    @Column(name = "menu_id")
    private String menuId;

    /**
     * 权限路径
     */
    @Column(name = "permission_url")
    private String permissionUrl;

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
    private String menuName;
    
    @Transient
    private Boolean checked = false;

    private static final long serialVersionUID = 1L;
}