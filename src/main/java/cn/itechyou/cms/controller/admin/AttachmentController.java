package cn.itechyou.cms.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itechyou.cms.common.BaseController;
import cn.itechyou.cms.common.SearchEntity;

@Controller
@RequestMapping("/admin/attachment")
public class AttachmentController extends BaseController {
	
	@RequestMapping("/list")
	public String toIndex(Model model ,SearchEntity params) {
		return "admin/attachment/list";
	}
}
