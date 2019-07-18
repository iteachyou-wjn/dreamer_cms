package cn.itechyou.blog.controller.admin;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.common.BaseController;
import cn.itechyou.blog.common.SearchEntity;
import cn.itechyou.blog.entity.Navigate;
import cn.itechyou.blog.entity.NavigatePage;
import cn.itechyou.blog.entity.Pages;
import cn.itechyou.blog.security.token.TokenManager;
import cn.itechyou.blog.service.NavigateService;
import cn.itechyou.blog.service.PagesService;
import cn.itechyou.blog.utils.UUIDUtils;
import cn.itechyou.blog.vo.NavigateVo;

@Controller
@RequestMapping("/admin/navigate")
public class NavigateController extends BaseController{

	@Autowired
	private NavigateService navigateService;
	@Autowired
	private PagesService pagesService;
	
	@RequestMapping("/list")
	public String list(Model model,SearchEntity params) {
		PageInfo<Navigate> page = navigateService.queryListByPage(params);
		model.addAttribute("navigates", page);
		return "/admin/navigate/list";
	}
	
	@RequestMapping("/toAdd")
	public String toAdd(Model model) {
		return "/admin/navigate/add";
	}
	
	@RequestMapping("/add")
	public String add(Navigate navigate) {
		navigate.setId(UUIDUtils.getPrimaryKey());
		navigate.setCode(UUIDUtils.getCharAndNumr(8));
		navigate.setCreateBy(TokenManager.getToken().getId());
		navigate.setCreateTime(new Date());
		navigateService.save(navigate);
		return "redirect:/admin/navigate/list";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(Model model,String id) {
		Navigate navigate = navigateService.selectById(id);
		List<Pages> pages = pagesService.queryAllShowPages();
		List<NavigateVo> treeList = navigateService.treeList(navigate.getId());
		model.addAttribute("navigate", navigate);
		model.addAttribute("pages", pages);
		model.addAttribute("treeList", treeList);
		return "/admin/navigate/edit";
	}
	
	@RequestMapping(value ="/edit")
	public String edit(Navigate navigate) {
		navigate.setUpdateBy(TokenManager.getToken().getId());
		navigate.setUpdateTime(new Date());
		int num = navigateService.update(navigate);
		return "redirect:/admin/navigate/list";
	}

	@RequestMapping(value ="/saveNavPage")
	@ResponseBody
	public void saveNavPage(@RequestBody NavigatePage[] navigatePages) {
		int num = navigateService.saveNavPage(navigatePages);
		List<NavigateVo> treeList = navigateService.treeList(navigatePages[0].getNavId());
		this.outJson(treeList);
	}
	
	@RequestMapping(value ="/updateOrderBy")
	@ResponseBody
	public void updateOrderBy(NavigatePage navigatePage) {
		navigateService.updateOrderBy(navigatePage);
		List<NavigateVo> treeList = navigateService.treeList(navigatePage.getNavId());
		this.outJson(treeList);
	}
	
	@RequestMapping(value ="/deletePage")
	@ResponseBody
	public void deletePage(NavigatePage navigatePage) {
		navigateService.deletePage(navigatePage);
		List<NavigateVo> treeList = navigateService.treeList(navigatePage.getNavId());
		this.outJson(treeList);
	}
	
	@RequestMapping(value ="/delete")
	public String delete(Model model,String id) {
		int row = navigateService.delete(id);
		return "redirect:/admin/navigate/list";
	}
}
