package cc.iteachyou.cms.controller.admin;

import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.Menu;
import cc.iteachyou.cms.entity.Permission;
import cc.iteachyou.cms.exception.AdminGeneralException;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.security.token.TokenManager;
import cc.iteachyou.cms.service.MenuService;
import cc.iteachyou.cms.service.PermissionService;
import cc.iteachyou.cms.utils.UUIDUtils;

/**
 * 权限管理
 * @author Administrator
 *
 */
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
	@Log(operType = OperatorType.PAGE, module = "权限管理", content = "权限分页列表")
	@RequestMapping({"","/list"})
	@RequiresPermissions("6g1755tx")
	public String list(Model model, SearchEntity params) {
		PageInfo<Permission> page = permissionService.queryListByPage(params);
		model.addAttribute("page", page);
		return "admin/permission/list";
	}

	/**
	 * 添加跳转
	 */
	@Log(operType = OperatorType.OTHER, module = "权限管理", content = "添加权限页面")
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
	@Log(operType = OperatorType.INSERT, module = "权限管理", content = "添加权限")
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
	@Log(operType = OperatorType.OTHER, module = "权限管理", content = "修改权限页面")
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
	@Log(operType = OperatorType.UPDATE, module = "权限管理", content = "修改权限")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@RequiresPermissions("0h4ejf33")
	public String update(Model model, Permission permission) {
		permissionService.update(permission);
		return "redirect:/admin/permission/list";
	}
	
	/**
	 * 删除
	 */
	@Log(operType = OperatorType.DELETE, module = "权限管理", content = "删除权限")
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@RequiresPermissions("qk8ogfi6")
	public String delete(Model model, String id) {
		permissionService.delete(id);
		return "redirect:/admin/permission/list";
	}
}
