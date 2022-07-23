package cc.iteachyou.cms.entity;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Table(name = "system_role_permission")
@Data
public class RolePermission implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private String roleId;

    /**
     * 权限ID
     */
    @Column(name = "permission_id")
    private String permissionId;

    /**
     * 类型（1：菜单；2：权限）
     */
    @Column(name = "type")
    private Integer type;

    private static final long serialVersionUID = 1L;
}