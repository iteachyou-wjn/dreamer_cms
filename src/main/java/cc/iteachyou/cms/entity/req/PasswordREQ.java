package cc.iteachyou.cms.entity.req;

import lombok.Data;

/**
 * 用户名密码实体
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class PasswordREQ {
	private String id;
	private String oldPwd;
	private String newPwd;
	private String reNewPwd;
}
