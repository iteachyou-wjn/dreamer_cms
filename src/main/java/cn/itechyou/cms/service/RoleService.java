package cn.itechyou.cms.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Role;
import cn.itechyou.cms.entity.RolePermission;
import cn.itechyou.cms.vo.PermissionVo;

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
