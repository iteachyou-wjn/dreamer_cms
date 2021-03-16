package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import cn.itechyou.cms.common.BaseMapper;
import cn.itechyou.cms.entity.Role;
import cn.itechyou.cms.vo.PermissionVo;

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