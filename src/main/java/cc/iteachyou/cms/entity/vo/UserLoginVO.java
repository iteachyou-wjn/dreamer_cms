package cc.iteachyou.cms.entity.vo;

import java.util.Date;

import lombok.Data;

@Data
public class UserLoginVO {
	private String id;

    private String username;

    private String mobile;

    private String realname;

    private Integer status;

    private String lastLoginIp;

    private Date lastLoginTime;

    private String portrait;
}
