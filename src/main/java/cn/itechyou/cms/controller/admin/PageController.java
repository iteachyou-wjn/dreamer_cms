package cn.itechyou.cms.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.BaseController;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Pages;
import cn.itechyou.cms.entity.PagesWithBLOBs;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.PagesService;
import cn.itechyou.cms.utils.UUIDUtils;

/**
 * 页面管理
 * @author Wangjn
 *
 */
@Controller
@RequestMapping("/admin/pages")
public class PageController extends BaseController {
	@Autowired
	private PagesService pagesService;
	
	@RequestMapping("/list")
	public String list(Model model, SearchEntity params) {
		Map<String,Object> map = new HashMap<>();
		PageInfo<Pages> pages = pagesService.queryListByPage(params);
		model.addAttribute("pages", pages);
		return "admin/pages/list";
	}
	
	
	@RequestMapping("/toAdd")
	public String toAdd(Model model) {
		return "admin/pages/add";
	}
	
	@RequestMapping("/add")
	public String add(PagesWithBLOBs page) {
		page.setId(UUIDUtils.getPrimaryKey());
		page.setCreateTime(new Date());
		page.setCreateBy(TokenManager.getToken().getId());
		page.setStatus(1);//显示
		page.setCode(UUIDUtils.getCharAndNumr(8));
		if("ue".equals(page.getDefaultEditor())) {
			page.setMdContent(null);
		}
		if(!page.getPageUrl().startsWith("/")) {
			page.setPageUrl("/" + page.getPageUrl());
		}
		
		int num = pagesService.save(page);
		return "redirect:/admin/pages/list";
	}
	
	@RequestMapping(value ="/delete")
	public String delete(String id) {
		int num = pagesService.delete(id);
		return "redirect:/admin/pages/list";
	}
	
	@RequestMapping(value = "/toEdit", method = RequestMethod.GET)
	public String toEdit(Model model, String id) {
		PagesWithBLOBs page = pagesService.queryPageById(id);
		model.addAttribute("page", page);
		return "admin/pages/edit";
	}
	
	@RequestMapping("/edit")
	public String edit(PagesWithBLOBs page) {
		page.setUpdateTime(new Date());
		page.setUpdateBy(TokenManager.getToken().getId());
		if("ue".equals(page.getDefaultEditor())) {
			page.setMdContent(null);
		}
		
		int num = pagesService.update(page);
		return "redirect:/admin/pages/list";
	}
	
}
