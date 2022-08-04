package cc.iteachyou.cms.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.entity.System;
import cc.iteachyou.cms.service.SystemService;

/**
 * 系统设置
 * @author Wangjn
 *
 */
@Controller
@RequestMapping("admin/system")
public class SystemController{

	@Autowired
	private SystemService systemService;
	
	/**
	 * 首页跳转
	 * @return
	 */
	@Log(operType = OperatorType.SELECT, module = "系统设置", content = "查询系统设置")
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
	@Log(operType = OperatorType.UPDATE, module = "系统设置", content = "修改系统设置")
	@RequestMapping("update")
	@RequiresPermissions("66e0j92s")
	public String update(System system) {
		int num = systemService.update(system);
		return "redirect:/admin/system/toIndex";
	}
}
