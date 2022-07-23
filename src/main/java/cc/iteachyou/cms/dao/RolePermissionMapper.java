package cc.iteachyou.cms.dao;

import java.util.List;

import cc.iteachyou.cms.common.BaseMapper;
import cc.iteachyou.cms.entity.RolePermission;

/**
 * RolePermissionMapper继承基类
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

	int insertBatchList(List<RolePermission> list);
}