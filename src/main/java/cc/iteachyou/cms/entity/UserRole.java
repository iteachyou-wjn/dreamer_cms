package cc.iteachyou.cms.entity;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Table(name = "system_user_role")
@Data
public class UserRole implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private String roleId;

    private static final long serialVersionUID = 1L;
}