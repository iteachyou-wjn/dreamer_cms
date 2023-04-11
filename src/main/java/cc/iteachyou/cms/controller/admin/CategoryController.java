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
import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.common.ResponseResult;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.common.StateCodeEnum;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.entity.Form;
import cc.iteachyou.cms.entity.System;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.exception.XssAndSqlException;
import cc.iteachyou.cms.security.token.TokenManager;
import cc.iteachyou.cms.service.CategoryService;
import cc.iteachyou.cms.service.FormService;
import cc.iteachyou.cms.service.SystemService;
import cc.iteachyou.cms.utils.RandomUtil;
import cc.iteachyou.cms.utils.StringUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

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
	@RequiresPermissions("system:category:page")
	public String list(Model model,SearchEntity params) {
		PageInfo<Category> page = categoryService.queryListByPage(params);
		model.addAttribute("page", page);
		return "admin/category/list";
	}
	
	@Log(operType = OperatorType.OTHER, module = "栏目管理", content = "添加栏目页面")
	@RequestMapping("/toAdd")
	@RequiresPermissions("system:category:toadd")
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
	@RequiresPermissions("system:category:toedit")
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
	@RequiresPermissions("system:category:add")
	public String add(Category category) throws CmsException {
		category.setId(IdUtil.getSnowflakeNextIdStr());
		category.setCode(RandomUtil.getCharAndNumr(8));
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
		
		String coverTemp = StrUtil.isBlank(category.getCoverTemp()) ? "" : category.getCoverTemp();
		String listTemp = StrUtil.isBlank(category.getListTemp()) ? "" : category.getListTemp();
		String articleTemp = StrUtil.isBlank(category.getArticleTemp()) ? "" : category.getArticleTemp();
		
		if(coverTemp.contains("../") || coverTemp.contains("..\\")) {
			throw new XssAndSqlException(
					ExceptionEnum.XSS_SQL_EXCEPTION.getCode(),
					ExceptionEnum.XSS_SQL_EXCEPTION.getMessage(),
					"栏目封面模板文件名疑似不安全，详情：" + category.getCoverTemp());
		}
		if(listTemp.contains("../") || listTemp.contains("..\\")) {
			throw new XssAndSqlException(
					ExceptionEnum.XSS_SQL_EXCEPTION.getCode(),
					ExceptionEnum.XSS_SQL_EXCEPTION.getMessage(),
					"栏目列表模板文件名疑似不安全，详情：" + category.getListTemp());
		}
		if(articleTemp.contains("../") || articleTemp.contains("..\\")) {
			throw new XssAndSqlException(
					ExceptionEnum.XSS_SQL_EXCEPTION.getCode(),
					ExceptionEnum.XSS_SQL_EXCEPTION.getMessage(),
					"栏目内容页模板文件名疑似不安全，详情：" + category.getArticleTemp());
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
	@RequiresPermissions("system:category:update")
	public String edit(Category category) throws CmsException {
		category.setUpdateBy(TokenManager.getToken().getId());
		category.setUpdateTime(new Date());
		
		String coverTemp = StrUtil.isBlank(category.getCoverTemp()) ? "" : category.getCoverTemp();
		String listTemp = StrUtil.isBlank(category.getListTemp()) ? "" : category.getListTemp();
		String articleTemp = StrUtil.isBlank(category.getArticleTemp()) ? "" : category.getArticleTemp();
		
		if(coverTemp.contains("../") || coverTemp.contains("..\\")) {
			throw new XssAndSqlException(
					ExceptionEnum.XSS_SQL_EXCEPTION.getCode(),
					ExceptionEnum.XSS_SQL_EXCEPTION.getMessage(),
					"栏目封面模板文件名疑似不安全，详情：" + category.getCoverTemp());
		}
		if(listTemp.contains("../") || listTemp.contains("..\\")) {
			throw new XssAndSqlException(
					ExceptionEnum.XSS_SQL_EXCEPTION.getCode(),
					ExceptionEnum.XSS_SQL_EXCEPTION.getMessage(),
					"栏目列表模板文件名疑似不安全，详情：" + category.getListTemp());
		}
		if(articleTemp.contains("../") || articleTemp.contains("..\\")) {
			throw new XssAndSqlException(
					ExceptionEnum.XSS_SQL_EXCEPTION.getCode(),
					ExceptionEnum.XSS_SQL_EXCEPTION.getMessage(),
					"栏目内容页模板文件名疑似不安全，详情：" + category.getArticleTemp());
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
		int num = categoryService.update(category);
		return "redirect:/admin/category/list";
	}
	
	@Log(operType = OperatorType.DELETE, module = "栏目管理", content = "删除栏目")
	@RequestMapping(value ="/delete")
	@RequiresPermissions("system:category:delete")
	public String delete(String id) throws CmsException {
		int num = categoryService.delete(id);
		return "redirect:/admin/category/list";
	}
	
	@Log(operType = OperatorType.SELECT, module = "栏目管理", content = "加载下级栏目")
	@RequestMapping(value = "/loadSon", method = RequestMethod.GET)
	@RequiresPermissions("system:category:page")
	public void loadSon(String id) {
		List<Category> list = categoryService.selectByParentId(id);
		ResponseResult result = ResponseResult.Factory.newInstance(Boolean.TRUE,
				StateCodeEnum.HTTP_SUCCESS.getCode(), list,
				StateCodeEnum.HTTP_SUCCESS.getDescription());
		this.outJson(result);
	}
	
	@Log(operType = OperatorType.UPDATE, module = "栏目管理", content = "修改栏目排序")
	@RequestMapping(value = "/updateSort")
	@RequiresPermissions("system:category:sort")
	@ResponseBody
	public void updateSort(@RequestBody List<Category> list) {
		categoryService.updateSort(list);
	}
}
