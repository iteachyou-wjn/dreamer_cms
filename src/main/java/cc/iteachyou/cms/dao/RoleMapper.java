package cc.iteachyou.cms.dao;

import java.util.List;
import java.util.Map;

import cc.iteachyou.cms.common.BaseMapper;
import cc.iteachyou.cms.entity.Role;
import cc.iteachyou.cms.vo.PermissionVo;

/**
 * RoleMapper继承基类
 */
public interface RoleMapper extends BaseMapper<Role> {

	List<Role> queryListByPage(Map<String, Object> entity);

	List<PermissionVo> selectPermissionsByRoleId(String roleId);

	List<String> selectRoleCodesByUserId(String userId);

	List<String> selectPermissionCodesByUserId(String userId);

	List<String> selectAllPermissionCodes();
}