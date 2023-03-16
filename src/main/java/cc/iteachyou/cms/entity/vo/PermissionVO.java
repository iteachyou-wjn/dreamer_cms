package cc.iteachyou.cms.entity.vo;

import lombok.Data;

@Data
public class PermissionVO {
	private String id;
	private String permissionName;
	private String parentId;
	private String type;
	private Boolean checked;
}
