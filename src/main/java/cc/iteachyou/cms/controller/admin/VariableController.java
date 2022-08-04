package cc.iteachyou.cms.controller.admin;

import java.util.Date;

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
import cc.iteachyou.cms.entity.Variable;
import cc.iteachyou.cms.exception.AdminGeneralException;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.security.token.TokenManager;
import cc.iteachyou.cms.service.VariableService;
import cc.iteachyou.cms.utils.UUIDUtils;

/**
 * @Description: 变量管理
 * @ClassName: VariableController
 * @author Wangjn
 * @date 2019年7月17日 下午1:53:51
 */
@Controller
@RequestMapping("admin/variable")
public class VariableController {
	@Autowired
	private VariableService variableService;

	/**
	 * 列表
	 */
	@Log(operType = OperatorType.PAGE, module = "变量管理", content = "变量分页列表")
	@RequestMapping({"","/list"})
	@RequiresPermissions("9dyk01d8")
	public String list(Model model, SearchEntity params) {
		PageInfo<Variable> page = variableService.queryListByPage(params);
		model.addAttribute("page", page);
		return "admin/variable/list";
	}

	/**
	 * 添加跳转
	 */
	@Log(operType = OperatorType.OTHER, module = "变量管理", content = "添加变量页面")
	@RequestMapping("/toAdd")
	@RequiresPermissions("q1tr6l39")
	public String toAdd(Model model) {
		return "admin/variable/add";
	}
	
	/**
	 * 添加
	 * @throws CmsException 
	 */
	@Log(operType = OperatorType.INSERT, module = "变量管理", content = "添加变量")
	@RequestMapping("/add")
	@RequiresPermissions("3epp9r3m")
	public String add(Model model, Variable variable) throws CmsException {
		
		Variable temp = variableService.queryVariableByName(variable.getItemName());
		if(temp != null) {
			throw new AdminGeneralException(
					ExceptionEnum.VARIABLE_EXIST_EXCEPTION.getCode(),
					ExceptionEnum.VARIABLE_EXIST_EXCEPTION.getMessage(),
					"变量名已经存在，请检查变量名。");
		}
		
		variable.setId(UUIDUtils.getPrimaryKey());
		variable.setCreateBy(TokenManager.getToken().getId());
		variable.setCreateTime(new Date());
		try {
			variableService.add(variable);
		} catch (Exception e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/variable/list";
	}

	/**
	 * 详情
	 */
	@Log(operType = OperatorType.SELECT, module = "变量管理", content = "变量详情")
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	@RequiresPermissions("45o778z0")
	public String details(Model model, String id) {
		Variable variable = variableService.queryVariableId(id);
		model.addAttribute("variable", variable);
		return "admin/variable/details";
	}
	
	/**
	 * 编辑
	 */
	@Log(operType = OperatorType.OTHER, module = "变量管理", content = "修改变量页面")
	@RequestMapping(value = "/toEdit", method = RequestMethod.GET)
	@RequiresPermissions("593blqyf")
	public String toEdit(Model model, String id) {
		Variable variable = variableService.queryVariableId(id);
		model.addAttribute("variable", variable);
		return "admin/variable/edit";
	}
	
	/**
	 * 修改
	 */
	@Log(operType = OperatorType.UPDATE, module = "变量管理", content = "修改变量")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@RequiresPermissions("5c4nj8lt")
	public String update(Model model, Variable variable) {
		variableService.updateVariable(variable);
		return "redirect:/admin/variable/list";
	}
	/**
	 * 删除
	 */
	@Log(operType = OperatorType.DELETE, module = "变量管理", content = "删除变量")
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@RequiresPermissions("6t6676m8")
	public String delete(Model model, String id) {
		variableService.deleteVariable(id);
		return "redirect:/admin/variable/list";
	}

}
