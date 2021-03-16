package cn.itechyou.cms.dao;

import java.util.List;

import cn.itechyou.cms.common.BaseMapper;
import cn.itechyou.cms.entity.RolePermission;

/**
 * RolePermissionMapper继承基类
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

	int insertBatchList(List<RolePermission> list);
}