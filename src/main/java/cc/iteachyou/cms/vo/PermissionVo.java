package cc.iteachyou.cms.vo;

import lombok.Data;

@Data
public class PermissionVo {
	private String id;
	private String permissionName;
	private String parentId;
	private String type;
	private Boolean checked;
}
