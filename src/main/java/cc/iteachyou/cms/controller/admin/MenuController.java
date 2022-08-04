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
import cc.iteachyou.cms.exception.AdminGeneralException;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.security.token.TokenManager;
import cc.iteachyou.cms.service.MenuService;
import cc.iteachyou.cms.utils.UUIDUtils;

/**
 * 菜单管理
 * @author 王俊南
 *
 */
@Controller
@RequestMapping("admin/menu")
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	/**
	 * 列表
	 */
	@Log(operType = OperatorType.PAGE, module = "菜单管理", content = "菜单分页列表")
	@RequestMapping({"","/list"})
	@RequiresPermissions("i17jcg5g")
	public String list(Model model, SearchEntity params) {
		PageInfo<Menu> page = menuService.queryListByPage(params);
		model.addAttribute("page", page);
		return "admin/menu/list";
	}

	/**
	 * 添加跳转
	 */
	@Log(operType = OperatorType.OTHER, module = "菜单管理", content = "添加菜单页面")
	@RequestMapping("/toAdd")
	@RequiresPermissions("n51w5y84")
	public String toAdd(Model model) {
		List<Menu> list = menuService.queryAll();
		model.addAttribute("parentList", list);
		return "admin/menu/add";
	}
	
	/**
	 * 添加
	 * @throws CmsException 
	 */
	@Log(operType = OperatorType.INSERT, module = "菜单管理", content = "添加菜单")
	@RequestMapping("/add")
	@RequiresPermissions("75m8k4mk")
	public String add(Model model, Menu menu) throws CmsException {
		menu.setId(UUIDUtils.getPrimaryKey());
		menu.setMenuCode(UUIDUtils.getCharAndNumr(8));
		menu.setCreateBy(TokenManager.getToken().getId());
		menu.setCreateTime(new Date());
		try {
			menuService.add(menu);
		} catch (Exception e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/menu/list";
	}
	
	/**
	 * 编辑
	 */
	@Log(operType = OperatorType.OTHER, module = "菜单管理", content = "修改菜单页面")
	@RequestMapping(value = "/toEdit", method = RequestMethod.GET)
	@RequiresPermissions("6sva81i7")
	public String toEdit(Model model, String id) {
		List<Menu> list = menuService.queryAll();
		Menu menu = menuService.queryMenuById(id);
		model.addAttribute("parentList", list);
		model.addAttribute("menu", menu);
		return "admin/menu/edit";
	}
	
	/**
	 * 修改
	 */
	@Log(operType = OperatorType.UPDATE, module = "菜单管理", content = "修改菜单")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@RequiresPermissions("43rk6390")
	public String update(Model model, Menu menu) {
		menuService.update(menu);
		return "redirect:/admin/menu/list";
	}
	/**
	 * 删除
	 */
	@Log(operType = OperatorType.DELETE, module = "菜单管理", content = "删除菜单")
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@RequiresPermissions("r55v68qg")
	public String delete(Model model, String id) {
		menuService.delete(id);
		return "redirect:/admin/menu/list";
	}
}
