package cn.itechyou.blog.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itechyou.blog.common.BaseController;
import cn.itechyou.blog.entity.System;
import cn.itechyou.blog.service.SystemService;

@Controller
@RequestMapping("/admin/system")
public class SystemController extends BaseController{

	@Autowired
	private SystemService systemService;
	/**
	 * 首页跳转
	 * 
	 * @return
	 */
	@RequestMapping("toIndex")
	public String toIndex(Model model) {
		
		System system = systemService.getSystemList();
		model.addAttribute("system", system);
		return "/admin/system/system";
	}
	
	/**
	 * 更新
	 * 
	 * @return
	 */
	@RequestMapping("update")
	public String update(System system) {
		
		int num = systemService.update(system);
		return "redirect:/admin/system/toIndex";
	}
}
