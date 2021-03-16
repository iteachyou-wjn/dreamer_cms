package cn.itechyou.cms.controller.admin;

import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.ExceptionEnum;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Variable;
import cn.itechyou.cms.exception.AdminGeneralException;
import cn.itechyou.cms.exception.CmsException;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.VariableService;
import cn.itechyou.cms.utils.UUIDUtils;

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
	@RequestMapping({"","/list"})
	@RequiresPermissions("9dyk01d8")
	public String list(Model model, SearchEntity params) {
		PageInfo<Variable> page = variableService.queryListByPage(params);
		model.addAttribute("variables", page);
		return "admin/variable/list";
	}

	/**
	 * 添加跳转
	 */
	@RequestMapping("/toAdd")
	@RequiresPermissions("q1tr6l39")
	public String toAdd(Model model) {
		return "admin/variable/add";
	}
	
	/**
	 * 添加
	 * @throws CmsException 
	 */
	@RequestMapping("/add")
	@RequiresPermissions("3epp9r3m")
	public String add(Model model, Variable variable) throws CmsException {
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
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@RequiresPermissions("5c4nj8lt")
	public String update(Model model, Variable variable) {
		variableService.updateVariable(variable);
		return "redirect:/admin/variable/list";
	}
	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@RequiresPermissions("6t6676m8")
	public String delete(Model model, String id) {
		variableService.deleteVariable(id);
		return "redirect:/admin/variable/list";
	}

}
