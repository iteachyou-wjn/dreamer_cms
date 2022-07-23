package cc.iteachyou.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.Constant;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.dao.MenuMapper;
import cc.iteachyou.cms.dao.RoleMapper;
import cc.iteachyou.cms.entity.Menu;
import cc.iteachyou.cms.service.MenuService;
import cc.iteachyou.cms.utils.StringUtil;

@Service
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuMapper menuMapper;
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public PageInfo<Menu> queryListByPage(SearchEntity params) {
		if(params.getPageNum() == null || params.getPageNum() == 0) {
			params.setPageNum(1);
		}
		if(params.getPageSize() == null || params.getPageSize() == 0) {
			params.setPageSize(10);
		}
		PageHelper.startPage(params.getPageNum(), params.getPageSize());
		List<Menu> list = menuMapper.queryListByPage(params.getEntity());
		PageInfo<Menu> page = new PageInfo<Menu>(list);
		return page;
	}

	@Override
	public int add(Menu menu) {
		if(StringUtil.isBlank(menu.getParentId())) {
			menu.setParentId(null);
		}
		return menuMapper.insertSelective(menu);
	}

	@Override
	public Menu queryMenuById(String id) {
		Menu menu = menuMapper.selectByPrimaryKey(id);
		if(StringUtil.isNotBlank(menu.getParentId()) && !"-1".equals(menu.getParentId())) {
			Menu parent = menuMapper.selectByPrimaryKey(menu.getParentId());
			menu.setParentMenuName(parent.getMenuName());
		}
		return menu;
	}

	@Override
	public int update(Menu menu) {
		return menuMapper.updateByPrimaryKeySelective(menu);
	}

	@Override
	public int delete(String id) {
		return menuMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<Menu> queryAll() {
		return menuMapper.selectAll();
	}

	@Override
	public List<Menu> queryListByUserId(String userId) {
		List<Menu> menus;
		List<String> roles = roleMapper.selectRoleCodesByUserId(userId);
		if(roles.contains(Constant.ADMIN_ROLE)) {
			menus = menuMapper.selectListByParentId("-1");
			menus = recursionGetMenus(menus);
		}else {
			menus = menuMapper.selectListByUserId(userId, "-1");
			menus = recursionGetMenus(userId, menus);
		}
		return menus;
	}
	
	/**
	 * 根据用户ID递归获取所拥有的菜单权限
	 * @param userId
	 * @param menus
	 * @return
	 */
	private List<Menu> recursionGetMenus(String userId, List<Menu> menus) {
		for(int i = 0;i < menus.size();i++) {
			Menu menu = menus.get(i);
			List<Menu> children = menuMapper.selectListByUserId(userId, menu.getId());
			children = recursionGetMenus(userId, children);
			menu.setChildren(children);
		}
		return menus;
	}
	
	/**
	 * 递归获取所有菜单权限
	 * @param menus
	 * @return
	 */
	private List<Menu> recursionGetMenus(List<Menu> menus) {
		for(int i = 0;i < menus.size();i++) {
			Menu menu = menus.get(i);
			List<Menu> children = menuMapper.selectListByParentId(menu.getId());
			children = recursionGetMenus(children);
			menu.setChildren(children);
		}
		return menus;
	}
}
