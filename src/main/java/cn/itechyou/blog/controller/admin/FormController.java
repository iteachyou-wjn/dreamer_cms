package cn.itechyou.blog.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.common.Constant;
import cn.itechyou.blog.common.SearchEntity;
import cn.itechyou.blog.entity.Field;
import cn.itechyou.blog.entity.Form;
import cn.itechyou.blog.exception.TransactionException;
import cn.itechyou.blog.security.token.TokenManager;
import cn.itechyou.blog.service.FieldService;
import cn.itechyou.blog.service.FormService;
import cn.itechyou.blog.utils.UUIDUtils;

@Controller
@RequestMapping("/admin/forms")
public class FormController {
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private FieldService fieldService;
	
	@RequestMapping("/list")
	public String toIndex(Model model ,SearchEntity params) {
		PageInfo<Form> forms = formService.queryListByPage(params);
		model.addAttribute("forms", forms);
		return "admin/forms/list";
	}
	
	@RequestMapping("/toAdd")
	public String toAdd() {
		return "admin/forms/add";
	}
	
	@RequestMapping("/add")
	public String add(Model model,Form form) {
		form.setId(UUIDUtils.getPrimaryKey());
		form.setCreateBy(TokenManager.getToken().getId());
		form.setCreateTime(new Date());
		form.setCode(UUIDUtils.getCharAndNumr(8));
		
		try {
			formService.add(form);
		} catch (TransactionException e) {
			model.addAttribute("exception", e);
			return Constant.ERROR;
		}
		return "redirect:/admin/forms/list";
	}
	
	@RequestMapping(value = "/toEdit", method = RequestMethod.GET)
	public String toEdit(Model model, String id) {
		Form form = formService.queryFormById(id);
		List<Field> fields = fieldService.queryFieldByFormId(id);

		model.addAttribute("form", form);
		model.addAttribute("fields", fields);
		
		return "admin/forms/edit";
	}
	
	@RequestMapping("/edit")
	public String edit(Model model,Form newForm) {
		Form oldForm = formService.queryFormById(newForm.getId());
		
		newForm.setUpdateBy(TokenManager.getToken().getId());
		newForm.setUpdateTime(new Date());
		
		try {
			formService.update(newForm,oldForm);
		} catch (TransactionException e) {
			model.addAttribute("exception", e);
			return Constant.ERROR;
		}
		return "redirect:/admin/forms/list";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(Model model, String id) {
		Form form = formService.queryFormById(id);
		if(form.getType() == 0) {
			throw new RuntimeException("系统表单不允许删除！");
		}
		//删除字段
		List<Field> fields = fieldService.queryFieldByFormId(id);
		//删除表
		try {
			formService.delete(form,fields);
		} catch (TransactionException e) {
			model.addAttribute("exception", e);
			return Constant.ERROR;
		}
		return "redirect:/admin/forms/list";
	}
}
