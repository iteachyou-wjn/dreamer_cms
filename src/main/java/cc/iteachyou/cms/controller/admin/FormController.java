package cc.iteachyou.cms.controller.admin;

import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.Field;
import cc.iteachyou.cms.entity.Form;
import cc.iteachyou.cms.exception.AdminGeneralException;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.exception.TransactionException;
import cc.iteachyou.cms.security.token.TokenManager;
import cc.iteachyou.cms.service.FieldService;
import cc.iteachyou.cms.service.FormService;
import cc.iteachyou.cms.utils.UUIDUtils;

/**
 * 表单管理
 * @author Wangjn
 *
 */
@Controller
@RequestMapping("admin/forms")
public class FormController {
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private FieldService fieldService;
	
	@Log(operType = OperatorType.PAGE, module = "表单模型", content = "表单模型分页列表")
	@RequestMapping({"","/list"})
	@RequiresPermissions("7pswu738")
	public String toIndex(Model model ,SearchEntity params) {
		PageInfo<Form> forms = formService.queryListByPage(params);
		model.addAttribute("page", forms);
		return "admin/forms/list";
	}
	
	@Log(operType = OperatorType.OTHER, module = "表单模型", content = "添加表单模型页面")
	@RequestMapping("/toAdd")
	@RequiresPermissions("w0kbe38p")
	public String toAdd() {
		return "admin/forms/add";
	}
	
	@Log(operType = OperatorType.INSERT, module = "表单模型", content = "添加表单模型")
	@RequestMapping("/add")
	@RequiresPermissions("fn9o6433")
	public String add(Model model,Form form) throws CmsException {
		form.setId(UUIDUtils.getPrimaryKey());
		form.setCreateBy(TokenManager.getToken().getId());
		form.setCreateTime(new Date());
		form.setCode(UUIDUtils.getCharAndNumr(8));
		
		try {
			formService.add(form);
		} catch (TransactionException e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/forms/list";
	}
	
	@Log(operType = OperatorType.OTHER, module = "表单模型", content = "修改表单模型页面")
	@RequestMapping(value = "/toEdit", method = RequestMethod.GET)
	@RequiresPermissions("u51mogha")
	public String toEdit(Model model, String id) {
		Form form = formService.queryFormById(id);
		List<Field> fields = fieldService.queryFieldByFormId(id);

		model.addAttribute("form", form);
		model.addAttribute("fields", fields);
		
		return "admin/forms/edit";
	}
	
	@Log(operType = OperatorType.UPDATE, module = "表单模型", content = "修改表单模型")
	@RequestMapping("/edit")
	@RequiresPermissions("19wh2wrf")
	public String edit(Model model,Form newForm) throws CmsException {
		Form oldForm = formService.queryFormById(newForm.getId());
		
		newForm.setUpdateBy(TokenManager.getToken().getId());
		newForm.setUpdateTime(new Date());
		
		try {
			formService.update(newForm,oldForm);
		} catch (TransactionException e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/forms/list";
	}
	
	@Log(operType = OperatorType.DELETE, module = "表单模型", content = "删除表单模型")
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@RequiresPermissions("3kc86164")
	public String delete(Model model, String id) throws CmsException {
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
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/forms/list";
	}
}
