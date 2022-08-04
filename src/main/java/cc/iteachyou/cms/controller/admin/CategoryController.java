package cc.iteachyou.cms.controller.admin;

import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.common.BaseController;
import cc.iteachyou.cms.common.ResponseResult;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.common.StateCodeEnum;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.entity.Form;
import cc.iteachyou.cms.entity.System;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.security.token.TokenManager;
import cc.iteachyou.cms.service.CategoryService;
import cc.iteachyou.cms.service.FormService;
import cc.iteachyou.cms.service.SystemService;
import cc.iteachyou.cms.utils.StringUtil;
import cc.iteachyou.cms.utils.UUIDUtils;

/**
 * 栏目管理
 * @author Wangjn
 */
@Controller
@RequestMapping("admin/category")
public class CategoryController extends BaseController{
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private SystemService systemService;
	
	@Log(operType = OperatorType.PAGE, module = "栏目管理", content = "栏目分页列表")
	@RequestMapping({"","/list"})
	@RequiresPermissions("up0t1rv4")
	public String list(Model model,SearchEntity params) {
		PageInfo<Category> page = categoryService.queryListByPage(params);
		model.addAttribute("page", page);
		return "admin/category/list";
	}
	
	@Log(operType = OperatorType.OTHER, module = "栏目管理", content = "添加栏目页面")
	@RequestMapping("/toAdd")
	@RequiresPermissions("o6499pg5")
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
	
	@Log(operType = OperatorType.OTHER, module = "栏目管理", content = "修改栏目页面")
	@RequestMapping("/toEdit")
	@RequiresPermissions("0157q6w4")
	public String toEdit(Model model,String id) {
		Category category = categoryService.selectById(id);
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
	
	@Log(operType = OperatorType.INSERT, module = "栏目管理", content = "添加栏目")
	@RequestMapping("/add")
	@RequiresPermissions("pdr1y803")
	public String add(Category category) {
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
	
	@Log(operType = OperatorType.UPDATE, module = "栏目管理", content = "修改栏目")
	@RequestMapping(value ="/edit")
	@RequiresPermissions("bira5jia")
	public String edit(Category category) {
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
	
	@Log(operType = OperatorType.DELETE, module = "栏目管理", content = "删除栏目")
	@RequestMapping(value ="/delete")
	@RequiresPermissions("56p8k0im")
	public String delete(String id) throws CmsException {
		int num = categoryService.delete(id);
		return "redirect:/admin/category/list";
	}
	
	@Log(operType = OperatorType.SELECT, module = "栏目管理", content = "加载下级栏目")
	@RequestMapping(value = "/loadSon", method = RequestMethod.GET)
	@RequiresPermissions("tvu49h42")
	public void loadSon(String id) {
		List<Category> list = categoryService.selectByParentId(id);
		ResponseResult result = ResponseResult.Factory.newInstance(Boolean.TRUE,
				StateCodeEnum.HTTP_SUCCESS.getCode(), list,
				StateCodeEnum.HTTP_SUCCESS.getDescription());
		this.outJson(result);
	}
	
	@Log(operType = OperatorType.UPDATE, module = "栏目管理", content = "修改栏目排序")
	@RequestMapping(value = "/updateSort")
	@RequiresPermissions("3ywkqhmv")
	@ResponseBody
	public void updateSort(@RequestBody List<Category> list) {
		categoryService.updateSort(list);
	}
}
