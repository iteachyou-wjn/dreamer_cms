package cn.itechyou.cms.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itechyou.cms.common.BaseController;
import cn.itechyou.cms.entity.System;
import cn.itechyou.cms.service.SystemService;

/**
 * 系统设置
 * @author Wangjn
 *
 */
@Controller
@RequestMapping("admin/system")
public class SystemController extends BaseController{

	@Autowired
	private SystemService systemService;
	/**
	 * 首页跳转
	 * @return
	 */
	@RequestMapping({"","toIndex"})
	@RequiresPermissions("8q1735f2")
	public String toIndex(Model model) {
		System system = systemService.getSystem();
		model.addAttribute("system", system);
		return "admin/system/system";
	}
	
	/**
	 * 更新
	 * @return
	 */
	@RequestMapping("update")
	@RequiresPermissions("66e0j92s")
	public String update(System system) {
		int num = systemService.update(system);
		return "redirect:/admin/system/toIndex";
	}
}
