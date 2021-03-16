package cn.itechyou.cms.dao;

import java.util.List;

import cn.itechyou.cms.common.BaseMapper;
import cn.itechyou.cms.entity.UserRole;

/**
 * UserRoleMapper继承基类
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {
	int insertBatchList(List<UserRole> userRoles);
}