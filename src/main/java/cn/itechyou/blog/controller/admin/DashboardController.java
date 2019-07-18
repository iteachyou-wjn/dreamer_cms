package cn.itechyou.blog.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("dashboard")
public class DashboardController {
	
	@RequestMapping("index")
	public ModelAndView jump() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/dashboard/index");
		return mv;
	}
}
