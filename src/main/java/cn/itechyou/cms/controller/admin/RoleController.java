package cn.itechyou.cms.controller.admin;

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

import cn.itechyou.cms.common.ExceptionEnum;
import cn.itechyou.cms.common.ResponseResult;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.common.StateCodeEnum;
import cn.itechyou.cms.entity.Role;
import cn.itechyou.cms.entity.RolePermission;
import cn.itechyou.cms.exception.AdminGeneralException;
import cn.itechyou.cms.exception.CmsException;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.RoleService;
import cn.itechyou.cms.utils.StringUtil;
import cn.itechyou.cms.utils.UUIDUtils;
import cn.itechyou.cms.vo.PermissionVo;

@Controller
@RequestMapping("admin/role")
public class RoleController {
	@Autowired
	private RoleService roleService;
	
	/**
	 * 列表
	 */
	@RequestMapping({"","/list"})
	@RequiresPermissions("7s73s67l")
	public String list(Model model, SearchEntity params) {
		PageInfo<Role> page = roleService.queryListByPage(params);
		model.addAttribute("pageInfo", page);
		return "admin/role/list";
	}

	/**
	 * 添加跳转
	 */
	@RequestMapping("/toAdd")
	@RequiresPermissions("8h9r1tin")
	public String toAdd(Model model) {
		return "admin/role/add";
	}
	
	/**
	 * 添加
	 * @throws CmsException 
	 */
	@RequestMapping("/add")
	@RequiresPermissions("sd5jepm0")
	public String add(Model model, Role role) throws CmsException {
		role.setId(UUIDUtils.getPrimaryKey());
		//如果编码为空，系统则生成编码
		if(StringUtil.isBlank(role.getRoleCode())) {
			role.setRoleCode(UUIDUtils.getCharAndNumr(8));
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
	@RequestMapping(value = "/toEdit", method = RequestMethod.GET)
	@RequiresPermissions("6g6w462l")
	public String toEdit(Model model, String id) {
		Role role = roleService.queryRoleById(id);
		model.addAttribute("role", role);
		return "admin/role/edit";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@RequiresPermissions("jg2066e5")
	public String update(Model model, Role role) {
		roleService.update(role);
		return "redirect:/admin/role/list";
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@RequiresPermissions("zr25t2e0")
	public String delete(Model model, String id) {
		roleService.delete(id);
		return "redirect:/admin/role/list";
	}
	
	/**
	 * 分配权限页面
	 */
	@RequestMapping(value = "/toGrant", method = RequestMethod.GET)
	@RequiresPermissions("63p5qb94")
	public String toGrant(Model model, String id) {
		Role role = roleService.queryRoleById(id);

		List<PermissionVo> permissions = roleService.queryPermissionsByRoleId(id);
		
		model.addAttribute("role", role);
		model.addAttribute("permissions", permissions);
		return "admin/role/grant";
	}
	
	@PostMapping("grant/{roleId}")
	@RequiresPermissions("ac71i30f")
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
