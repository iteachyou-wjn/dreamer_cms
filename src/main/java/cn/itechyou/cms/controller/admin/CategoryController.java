package cn.itechyou.cms.controller.admin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.BaseController;
import cn.itechyou.cms.common.ResponseResult;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.common.StateCodeEnum;
import cn.itechyou.cms.entity.Category;
import cn.itechyou.cms.entity.CategoryWithBLOBs;
import cn.itechyou.cms.entity.Form;
import cn.itechyou.cms.entity.System;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.CategoryService;
import cn.itechyou.cms.service.FormService;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.utils.StringUtil;
import cn.itechyou.cms.utils.UUIDUtils;

@Controller
@RequestMapping("/admin/category")
public class CategoryController extends BaseController{
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private SystemService systemService;
	
	@RequestMapping("/list")
	public String list(Model model,SearchEntity params) {
		PageInfo<CategoryWithBLOBs> page = categoryService.queryListByPage(params);
		model.addAttribute("categorys", page);
		return "admin/category/list";
	}
	
	@RequestMapping("/toAdd")
	public String toAdd(Model model,String id) {
		Category category = null;
		if(id.equals("-1")) {
			category = new Category();
			category.setParentId("-1");
			category.setLevel("1");
			category.setCnname("顶级栏目");
		}else {
			category = categoryService.selectById(id);
			category.setLevel((Integer.parseInt(category.getLevel())+1)+"");
		}
		
		List<Form> forms = formService.queryAll();
		
		model.addAttribute("category", category);
		model.addAttribute("forms", forms);
		return "admin/category/add";
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(Model model,String id) {
		CategoryWithBLOBs category = categoryService.selectById(id);
		if(category.getParentId().equals("-1")) {
			category.setParentName("顶级栏目");
		}else {
			Category category2 = categoryService.selectById(category.getParentId());
			category.setParentName(category2.getCnname());
		}
		
		List<Form> forms = formService.queryAll();
		System system = systemService.getSystem();
		model.addAttribute("category", category);
		model.addAttribute("forms", forms);
		model.addAttribute("system", system);
		return "admin/category/edit";
	}
	
	@RequestMapping("/add")
	public String add(CategoryWithBLOBs category) {
		category.setId(UUIDUtils.getPrimaryKey());
		category.setCode(UUIDUtils.getCharAndNumr(8));
		category.setLevel(category.getParentId().equals("-1")?"1":category.getLevel());
		category.setParentId(StringUtil.isBlank(category.getParentId())? "-1" : category.getParentId());
		category.setCreateBy(TokenManager.getToken().getId());
		category.setCreateTime(new Date());
		if(!"-1".equals(category.getParentId())) {
			Category parent = categoryService.selectById(category.getParentId());
			category.setCatSeq(parent.getCatSeq() + "." + category.getCode());
		}else {
			category.setCatSeq("." + category.getCode());
		}
		
		//处理模版
		if(StringUtil.isNotBlank(category.getCoverTemp()) && !category.getCoverTemp().startsWith("/")) {
			category.setCoverTemp("/" + category.getCoverTemp());
		}
		if(StringUtil.isNotBlank(category.getListTemp()) && !category.getListTemp().startsWith("/")) {
			category.setListTemp("/" + category.getListTemp());
		}
		if(StringUtil.isNotBlank(category.getArticleTemp()) && !category.getArticleTemp().startsWith("/")) {
			category.setArticleTemp("/" + category.getArticleTemp());
		}
		categoryService.save(category);
		return "redirect:/admin/category/list";
	}
	
	@RequestMapping(value ="/edit")
	public String edit(CategoryWithBLOBs category) {
		category.setUpdateBy(TokenManager.getToken().getId());
		category.setUpdateTime(new Date());
		//处理模版
		if(StringUtil.isNotBlank(category.getCoverTemp()) && !category.getCoverTemp().startsWith("/")) {
			category.setCoverTemp("/" + category.getCoverTemp());
		}
		if(StringUtil.isNotBlank(category.getListTemp()) && !category.getListTemp().startsWith("/")) {
			category.setListTemp("/" + category.getListTemp());
		}
		if(StringUtil.isNotBlank(category.getArticleTemp()) && !category.getArticleTemp().startsWith("/")) {
			category.setArticleTemp("/" + category.getArticleTemp());
		}
		int num = categoryService.update(category);
		return "redirect:/admin/category/list";
	}
	
	@RequestMapping(value ="/delete")
	public String delete(String id) {
		int num = categoryService.delete(id);
		return "redirect:/admin/category/list";
	}
	
	@RequestMapping(value = "/loadSon", method = RequestMethod.GET)
	public void loadSon(String id) {
		List<CategoryWithBLOBs> list = categoryService.selectByParentId(id);
		ResponseResult result = ResponseResult.Factory.newInstance(Boolean.TRUE,
				StateCodeEnum.HTTP_SUCCESS.getCode(), list,
				StateCodeEnum.HTTP_SUCCESS.getDescription());
		this.outJson(result);
	}
	
	@RequestMapping(value = "/updateSort")
	@ResponseBody
	public void updateSort(@RequestBody List<Category> list) {
		categoryService.updateSort(list);
	}
}
