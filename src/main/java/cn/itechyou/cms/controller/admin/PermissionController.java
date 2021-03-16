package cn.itechyou.cms.controller.admin;

import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.ExceptionEnum;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Menu;
import cn.itechyou.cms.entity.Permission;
import cn.itechyou.cms.exception.AdminGeneralException;
import cn.itechyou.cms.exception.CmsException;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.MenuService;
import cn.itechyou.cms.service.PermissionService;
import cn.itechyou.cms.utils.UUIDUtils;

@Controller
@RequestMapping("admin/permission")
public class PermissionController {
	@Autowired
	private MenuService menuService;
	@Autowired
	private PermissionService permissionService;
	
	/**
	 * 列表
	 */
	@RequestMapping({"","/list"})
	@RequiresPermissions("6g1755tx")
	public String list(Model model, SearchEntity params) {
		PageInfo<Permission> page = permissionService.queryListByPage(params);
		model.addAttribute("pageInfo", page);
		return "admin/permission/list";
	}

	/**
	 * 添加跳转
	 */
	@RequestMapping("/toAdd")
	@RequiresPermissions("d00f103e")
	public String toAdd(Model model) {
		List<Menu> list = menuService.queryAll();
		model.addAttribute("menuList", list);
		return "admin/permission/add";
	}
	
	/**
	 * 添加
	 * @throws CmsException 
	 */
	@RequestMapping("/add")
	@RequiresPermissions("088livwa")
	public String add(Model model, Permission permission) throws CmsException {
		permission.setId(UUIDUtils.getPrimaryKey());
		permission.setPermissionCode(UUIDUtils.getCharAndNumr(8));
		permission.setCreateBy(TokenManager.getToken().getId());
		permission.setCreateTime(new Date());
		try {
			permissionService.add(permission);
		} catch (Exception e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/permission/list";
	}
	
	/**
	 * 编辑
	 */
	@RequestMapping(value = "/toEdit", method = RequestMethod.GET)
	@RequiresPermissions("8f63055b")
	public String toEdit(Model model, String id) {
		List<Menu> list = menuService.queryAll();
		Permission permission = permissionService.queryMenuById(id);
		model.addAttribute("menuList", list);
		model.addAttribute("permission", permission);
		return "admin/permission/edit";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@RequiresPermissions("0h4ejf33")
	public String update(Model model, Permission permission) {
		permissionService.update(permission);
		return "redirect:/admin/permission/list";
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@RequiresPermissions("qk8ogfi6")
	public String delete(Model model, String id) {
		permissionService.delete(id);
		return "redirect:/admin/permission/list";
	}
}
