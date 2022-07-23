package cc.iteachyou.cms.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.Role;
import cc.iteachyou.cms.entity.RolePermission;
import cc.iteachyou.cms.vo.PermissionVo;

public interface RoleService {

	PageInfo<Role> queryListByPage(SearchEntity params);

	int add(Role role);

	Role queryRoleById(String id);

	int update(Role role);

	int delete(String id);

	List<Role> queryAll();

	List<PermissionVo> queryPermissionsByRoleId(String id);

	int grant(String roleId, List<RolePermission> list);

	List<String> queryRoleCodesByUserId(String userId);

	List<String> queryPermissionCodesByUserId(String userId);

	List<String> queryAllPermissionCodes();

}
