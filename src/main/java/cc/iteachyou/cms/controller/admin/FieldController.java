package cc.iteachyou.cms.controller.admin;

import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.entity.Field;
import cc.iteachyou.cms.entity.Form;
import cc.iteachyou.cms.exception.AdminGeneralException;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.exception.TransactionException;
import cc.iteachyou.cms.security.token.TokenManager;
import cc.iteachyou.cms.service.FieldService;
import cc.iteachyou.cms.service.FormService;
import cc.iteachyou.cms.utils.StringUtil;
import cc.iteachyou.cms.utils.UUIDUtils;

/**
 * 字段管理
 * @author Wangjn
 *
 */
@Controller
@RequestMapping("admin/field")
public class FieldController {
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private FieldService fieldService;
	
	@Log(operType = OperatorType.OTHER, module = "表单模型", content = "添加字段页面")
	@RequestMapping("/toAdd")
	@RequiresPermissions("02srqt14")
	public String toAdd(Model model, String formId) {
		model.addAttribute("formId", formId);
		return "admin/field/add";
	}
	
	@Log(operType = OperatorType.INSERT, module = "表单模型", content = "添加字段")
	@RequestMapping("/add")
	@RequiresPermissions("8435902p")
	public String add(Model model,Field field) throws CmsException {
		field.setId(UUIDUtils.getPrimaryKey());
		field.setCreateBy(TokenManager.getToken().getId());
		field.setCreateTime(new Date());
		if(StringUtil.isBlank(field.getMaxLength())) {
			field.setMaxLength(200);
		}
		try {
			fieldService.add(field);
		} catch (TransactionException e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/forms/toEdit?id=" + field.getFormId();
	}
	
	@Log(operType = OperatorType.OTHER, module = "表单模型", content = "修改字段页面")
	@RequestMapping(value = "/toEdit", method = RequestMethod.GET)
	@RequiresPermissions("n6mszszy")
	public String toEdit(Model model, String id) {
		Field field = fieldService.queryFieldById(id);
		Form form = formService.queryFormById(field.getFormId());

		model.addAttribute("form", form);
		model.addAttribute("field", field);
		
		return "admin/field/edit";
	}
	
	@Log(operType = OperatorType.UPDATE, module = "表单模型", content = "修改字段")
	@RequestMapping("/edit")
	@RequiresPermissions("2f830o48")
	public String edit(Model model,Field newField) throws CmsException {
		Form form = formService.queryFormById(newField.getFormId());
		
		Field oldField = fieldService.queryFieldById(newField.getId());
		newField.setUpdateBy(TokenManager.getToken().getId());
		newField.setUpdateTime(new Date());
		if(StringUtil.isBlank(newField.getMaxLength())) {
			newField.setMaxLength(200);
		}
		try {
			fieldService.update(newField,form,oldField);
		} catch (TransactionException e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/forms/toEdit?id=" + newField.getFormId();
	}
	
	@Log(operType = OperatorType.DELETE, module = "表单模型", content = "删除字段")
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@RequiresPermissions("602lmb6w")
	public String delete(Model model, String id) throws CmsException {
		Field field = fieldService.queryFieldById(id);
		Form form = formService.queryFormById(field.getFormId());
		if(form.getType() == 0) {
			throw new RuntimeException("系统表单不允许删除！");
		}
		try {
			fieldService.delete(form,field);
		} catch (TransactionException e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/forms/toEdit?id=" + field.getFormId();
	}
}
