package cn.itechyou.blog.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itechyou.blog.common.BaseController;
import cn.itechyou.blog.common.SearchEntity;

@Controller
@RequestMapping("/admin/attachment")
public class AttachmentController extends BaseController {
	
	@RequestMapping("/list")
	public String toIndex(Model model ,SearchEntity params) {
		
		return "/admin/attachment/list";
	}
}
