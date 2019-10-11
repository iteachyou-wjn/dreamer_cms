package cn.itechyou.cms.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController {
	
	@RequestMapping("toIndex")
	public ModelAndView jump() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/dashboard/index");
		return mv;
	}
}
