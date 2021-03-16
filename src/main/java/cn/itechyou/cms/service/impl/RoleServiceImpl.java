package cn.itechyou.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.dao.RoleMapper;
import cn.itechyou.cms.dao.RolePermissionMapper;
import cn.itechyou.cms.entity.Role;
import cn.itechyou.cms.entity.RolePermission;
import cn.itechyou.cms.service.RoleService;
import cn.itechyou.cms.utils.UUIDUtils;
import cn.itechyou.cms.vo.PermissionVo;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RolePermissionMapper rolePermissionMapper;

	@Override
	public PageInfo<Role> queryListByPage(SearchEntity params) {
		if(params.getPageNum() == null || params.getPageNum() == 0) {
			params.setPageNum(1);
		}
		if(params.getPageSize() == null || params.getPageSize() == 0) {
			params.setPageSize(10);
		}
		PageHelper.startPage(params.getPageNum(), params.getPageSize());
		List<Role> list = roleMapper.queryListByPage(params.getEntity());
		PageInfo<Role> page = new PageInfo<Role>(list);
		return page;
	}

	@Override
	public int add(Role role) {
		return roleMapper.insertSelective(role);
	}

	@Override
	public Role queryRoleById(String id) {
		return roleMapper.selectByPrimaryKey(id);
	}

	@Override
	public int update(Role role) {
		return roleMapper.updateByPrimaryKeySelective(role);
	}

	@Override
	public int delete(String id) {
		return roleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<Role> queryAll() {
		return roleMapper.selectAll();
	}

	@Override
	public List<PermissionVo> queryPermissionsByRoleId(String id) {
		return roleMapper.selectPermissionsByRoleId(id);
	}

	@Override
	@Transactional
	public int grant(String roleId, List<RolePermission> list) {
		RolePermission example = new RolePermission();
		example.setRoleId(roleId);
		rolePermissionMapper.delete(example);
		for(int i = 0;i < list.size();i++) {
			RolePermission rp = list.get(i);
			rp.setId(UUIDUtils.getPrimaryKey());
		}
		int i = 0;
		if(list.size() > 0) {
			i = rolePermissionMapper.insertBatchList(list);
		}
		return 0;
	}

	/**
	 * 根据用户ID查询所拥有的角色
	 */
	@Override
	public List<String> queryRoleCodesByUserId(String userId) {
		return roleMapper.selectRoleCodesByUserId(userId);
	}
	
	/**
	 * 根据用户ID查询所拥有的权限
	 */
	@Override
	public List<String> queryPermissionCodesByUserId(String userId) {
		return roleMapper.selectPermissionCodesByUserId(userId);
	}

	@Override
	public List<String> queryAllPermissionCodes() {
		return roleMapper.selectAllPermissionCodes();
	}

}
