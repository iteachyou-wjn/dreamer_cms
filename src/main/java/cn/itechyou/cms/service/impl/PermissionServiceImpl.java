package cn.itechyou.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.dao.MenuMapper;
import cn.itechyou.cms.dao.PermissionMapper;
import cn.itechyou.cms.entity.Menu;
import cn.itechyou.cms.entity.Permission;
import cn.itechyou.cms.service.PermissionService;
import cn.itechyou.cms.utils.StringUtil;

@Service
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	private MenuMapper menuMapper;
	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public PageInfo<Permission> queryListByPage(SearchEntity params) {
		if(params.getPageNum() == null || params.getPageNum() == 0) {
			params.setPageNum(1);
		}
		if(params.getPageSize() == null || params.getPageSize() == 0) {
			params.setPageSize(10);
		}
		PageHelper.startPage(params.getPageNum(), params.getPageSize());
		List<Permission> list = permissionMapper.queryListByPage(params.getEntity());
		PageInfo<Permission> page = new PageInfo<Permission>(list);
		return page;
	}

	@Override
	public int add(Permission permission) {
		return permissionMapper.insertSelective(permission);
	}

	@Override
	public Permission queryMenuById(String id) {
		Permission permission = permissionMapper.selectByPrimaryKey(id);
		if(StringUtil.isNotBlank(permission.getMenuId())) {
			Menu menu = menuMapper.selectByPrimaryKey(permission.getMenuId());
			permission.setMenuName(menu.getMenuName());
		}
		return permission;
	}

	@Override
	public int update(Permission permission) {
		return permissionMapper.updateByPrimaryKeySelective(permission);
	}

	@Override
	public int delete(String id) {
		return permissionMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<Permission> queryAll() {
		return permissionMapper.selectAll();
	}
}
