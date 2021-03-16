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
import cn.itechyou.cms.exception.AdminGeneralException;
import cn.itechyou.cms.exception.CmsException;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.MenuService;
import cn.itechyou.cms.utils.UUIDUtils;

@Controller
@RequestMapping("admin/menu")
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	/**
	 * 列表
	 */
	@RequestMapping({"","/list"})
	@RequiresPermissions("i17jcg5g")
	public String list(Model model, SearchEntity params) {
		PageInfo<Menu> page = menuService.queryListByPage(params);
		model.addAttribute("pageInfo", page);
		return "admin/menu/list";
	}

	/**
	 * 添加跳转
	 */
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
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@RequiresPermissions("43rk6390")
	public String update(Model model, Menu menu) {
		menuService.update(menu);
		return "redirect:/admin/menu/list";
	}
	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@RequiresPermissions("r55v68qg")
	public String delete(Model model, String id) {
		menuService.delete(id);
		return "redirect:/admin/menu/list";
	}
}
