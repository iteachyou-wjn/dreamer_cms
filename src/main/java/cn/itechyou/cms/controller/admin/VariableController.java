package cn.itechyou.cms.controller.admin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.Constant;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Variable;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.VariableService;
import cn.itechyou.cms.utils.UUIDUtils;

/**
 * @Description: 变量管理
 * @ClassName: VariableController
 * @author Administrator
 * @date 2019年7月17日 下午1:53:51
 */
@Controller
@RequestMapping("/admin/variable")
public class VariableController {
	@Autowired
	private VariableService variableService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public String list(Model model, SearchEntity params) {
		PageInfo<Variable> page = variableService.queryListByPage(params);
		model.addAttribute("variables", page);
		return "admin/variable/list";
	}

	/**
	 * 添加跳转
	 */
	@RequestMapping("/toAdd")
	public String toAdd(Model model) {
		return "admin/variable/add";
	}
	
	/**
	 * 添加
	 */
	@RequestMapping("/add")
	public String add(Model model, Variable variable) {
		variable.setId(UUIDUtils.getPrimaryKey());
		variable.setCreateBy(TokenManager.getToken().getId());
		variable.setCreateTime(new Date());
		try {
			variableService.add(variable);
		} catch (Exception e1) {
			model.addAttribute("exception", e1);
			return Constant.ERROR;
		}
		return "redirect:/admin/variable/list";
	}

	/**
	 * 详情
	 */
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public String details(Model model, String id) {
		Variable variable = variableService.queryVariableId(id);
		model.addAttribute("variable", variable);
		return "admin/variable/details";
	}
	
	/**
	 * 编辑
	 */
	@RequestMapping(value = "/toEdit", method = RequestMethod.GET)
	public String toEdit(Model model, String id) {
		Variable variable = variableService.queryVariableId(id);
		model.addAttribute("variable", variable);
		return "admin/variable/edit";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Model model, Variable variable) {
		variableService.updateVariable(variable);
		return "redirect:/admin/variable/list";
	}
	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(Model model, String id) {
		variableService.deleteVariable(id);
		return "redirect:/admin/variable/list";
	}

}
