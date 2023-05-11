package cc.iteachyou.cms.controller.admin;

import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import cc.iteachyou.cms.entity.Role;
import cc.iteachyou.cms.entity.RolePermission;
import cc.iteachyou.cms.entity.vo.PermissionVO;
import cc.iteachyou.cms.exception.AdminGeneralException;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.security.token.TokenManager;
import cc.iteachyou.cms.service.RoleService;
import cc.iteachyou.cms.utils.StringUtil;
import cc.iteachyou.cms.utils.RandomUtil;
import cn.hutool.core.util.IdUtil;

/**
 * 角色管理
 * @author 王俊南
 *
 */
@Controller
@RequestMapping("admin/role")
public class RoleController extends BaseController {
	@Autowired
	private RoleService roleService;
	
	/**
	 * 列表
	 */
	@Log(operType = OperatorType.PAGE, module = "角色管理", content = "角色管理分页列表")
	@RequestMapping({"","/list"})
	@RequiresPermissions("system:role:page")
	public String list(Model model, SearchEntity params) {
		PageInfo<Role> page = roleService.queryListByPage(params);
		model.addAttribute("page", page);
		return "admin/role/list";
	}

	/**
	 * 添加跳转
	 */
	@Log(operType = OperatorType.OTHER, module = "角色管理", content = "添加角色页面")
	@RequestMapping("/toAdd")
	@RequiresPermissions("system:role:toadd")
	public String toAdd(Model model) {
		return "admin/role/add";
	}
	
	/**
	 * 添加
	 * @throws CmsException 
	 */
	@Log(operType = OperatorType.INSERT, module = "角色管理", content = "添加角色")
	@RequestMapping("/add")
	@RequiresPermissions("system:role:add")
	public String add(Model model, Role role) throws CmsException {
		role.setId(IdUtil.getSnowflakeNextIdStr());
		//如果编码为空，系统则生成编码
		if(StringUtil.isBlank(role.getRoleCode())) {
			role.setRoleCode(RandomUtil.getCharAndNumr(8));
		}
		role.setCreateBy(TokenManager.getToken().getId());
		role.setCreateTime(new Date());
		try {
			roleService.add(role);
		} catch (Exception e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/role/list";
	}
	
	/**
	 * 编辑
	 */
	@Log(operType = OperatorType.OTHER, module = "角色管理", content = "修改角色页面")
	@RequestMapping(value = "/toEdit", method = RequestMethod.GET)
	@RequiresPermissions("system:role:toedit")
	public String toEdit(Model model, String id) {
		Role role = roleService.queryRoleById(id);
		model.addAttribute("role", role);
		return "admin/role/edit";
	}
	
	/**
	 * 修改
	 */
	@Log(operType = OperatorType.UPDATE, module = "角色管理", content = "修改角色")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@RequiresPermissions("system:role:update")
	public String update(Model model, Role role) {
		roleService.update(role);
		return "redirect:/admin/role/list";
	}
	
	/**
	 * 删除
	 */
	@Log(operType = OperatorType.DELETE, module = "角色管理", content = "删除角色")
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@RequiresPermissions("system:role:delete")
	public String delete(Model model, String id) {
		roleService.delete(id);
		return "redirect:/admin/role/list";
	}
	
	/**
	 * 分配权限页面
	 */
	@Log(operType = OperatorType.OTHER, module = "角色管理", content = "角色分配权限")
	@RequestMapping(value = "/toGrant", method = RequestMethod.GET)
	@RequiresPermissions("system:role:togrant")
	public String toGrant(Model model, String id) {
		Role role = roleService.queryRoleById(id);

		List<PermissionVO> permissions = roleService.queryPermissionsByRoleId(id);
		
		model.addAttribute("role", role);
		model.addAttribute("permissions", permissions);
		return "admin/role/grant";
	}
	
	@Log(operType = OperatorType.GRANT, module = "角色管理", content = "角色分配权限")
	@PostMapping("grant/{roleId}")
	@RequiresPermissions("system:role:grant")
	@ResponseBody
	public ResponseResult grant(@PathVariable String roleId, @RequestBody List<RolePermission> list) {
		ResponseResult result = null;
		int i = roleService.grant(roleId, list);
		result = ResponseResult.Factory.newInstance(Boolean.TRUE,
				StateCodeEnum.HTTP_SUCCESS.getCode(), null,
				StateCodeEnum.HTTP_SUCCESS.getDescription());
		return result;
	}
}
