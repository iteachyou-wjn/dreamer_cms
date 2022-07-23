package cc.iteachyou.cms.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Table(name = "system_role")
@Data
public class Role implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 角色编码
     */
    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_time")
    private Date updateTime;
    
    @Transient
    private Boolean checked = false;

    private static final long serialVersionUID = 1L;
}