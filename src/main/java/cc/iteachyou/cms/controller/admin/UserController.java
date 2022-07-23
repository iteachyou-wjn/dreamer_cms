package cc.iteachyou.cms.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.common.ResponseResult;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.common.StateCodeEnum;
import cc.iteachyou.cms.entity.Role;
import cc.iteachyou.cms.entity.User;
import cc.iteachyou.cms.entity.UserRole;
import cc.iteachyou.cms.exception.AdminGeneralException;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.security.token.TokenManager;
import cc.iteachyou.cms.service.RoleService;
import cc.iteachyou.cms.service.UserService;
import cc.iteachyou.cms.utils.LoggerUtils;
import cc.iteachyou.cms.utils.StringUtil;
import cc.iteachyou.cms.utils.UUIDUtils;
import cc.iteachyou.cms.vo.UserPasswordVO;

/**
 * 用户管理
 * @author 王俊南
 * @date 2018-07-30
 *
 */
@Controller
@RequestMapping("admin/user")
public class UserController {
	private Logger logger = LoggerUtils.getPlatformLogger();
	
	@Resource
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	@RequestMapping({"","list"})
	@RequiresPermissions("498jkr41")
	public ModelAndView list(Model model, SearchEntity searchEntity) {
		ModelAndView mv = new ModelAndView();
		PageInfo<User> page = userService.listByPage(searchEntity);
		mv.addObject("page", page);
		mv.setViewName("admin/user/list");
		return mv;
	}
	
	@RequestMapping("toAdd")
	@RequiresPermissions("db9xb4dy")
	public ModelAndView toAdd() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/user/add");
		return mv;
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 * @throws CmsException
	 */
	@RequestMapping("add")
	@RequiresPermissions("00esg6hw")
	public String add(User user) throws CmsException {
		User temp = userService.getByUserName(user.getUsername());
		if(temp != null) {
			throw new AdminGeneralException(
					ExceptionEnum.USERNAME_EXIST_EXCEPTION.getCode(),
					ExceptionEnum.USERNAME_EXIST_EXCEPTION.getMessage(),
					"输入的用户名已经存在，请输入其它用户名再次尝试。");
		}
		user.setId(UUIDUtils.getPrimaryKey());
		user.setCreateBy(TokenManager.getToken().getId());
		user.setCreateTime(new Date());
		ByteSource salt = ByteSource.Util.bytes(user.getUsername() + user.getPassword());
		SimpleHash sh = new SimpleHash("MD5", user.getPassword(), salt, 1024);
		user.setPassword(sh.toString());
		user.setSalt(salt.toString());
		try {
			int i = userService.addUser(user);
		} catch (Exception e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/user/list";
	}
	
	@RequestMapping("toEdit")
	@RequiresPermissions("f9wezq49")
	public ModelAndView toEdit(String id) {
		ModelAndView mv = new ModelAndView();
		User user = userService.getByID(id);
		mv.addObject("user", user);
		mv.setViewName("admin/user/edit");
		return mv;
	}
	
	/**
	 * 修改用户 
	 * @param user
	 * @return
	 * @throws CmsException
	 */
	@RequestMapping("update")
	@RequiresPermissions("q85s17tm")
	public String update(User user) throws CmsException {
		user.setUpdateBy(TokenManager.getToken().getId());
		user.setUpdateTime(new Date());
		try {
			int i = userService.updateUser(user);
		} catch (Exception e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/user/list";
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@RequiresPermissions("p39sena0")
	public String delete(Model model, String id) {
		userService.deleteUser(id);
		return "redirect:/admin/user/list";
	}
	
	/**
	 * 跳转分配角色页面
	 */
	@RequestMapping(value = "/toGrant", method = RequestMethod.GET)
	@RequiresPermissions("2tvig6l7")
	public ModelAndView toGrant(Model model, String userId) {
		ModelAndView mv = new ModelAndView();
		User user = userService.getByID(userId);
		List<Role> roles = roleService.queryAll();
		
		//查询当前用户拥有的角色
		List<UserRole> userRoles = userService.queryRolesByUserId(userId);
		
		for(int i = 0;i < roles.size();i++) {
			for(int j = 0;j < userRoles.size();j++) {
				if(roles.get(i).getId().equals(userRoles.get(j).getRoleId())) {
					roles.get(i).setChecked(true);
				}
			}
		}
		model.addAttribute("user", user);
		model.addAttribute("roles", roles);
		mv.setViewName("admin/user/grant");
		return mv;
	}
	
	/**
	 * 分配角色
	 * @throws CmsException 
	 */
	@RequestMapping(value = "/grant", method = RequestMethod.POST)
	@RequiresPermissions("2o2sny2j")
	public String grant(Model model, String userId, @RequestParam(value = "roles", required = false) List<String> roles) throws CmsException {
		try {
			if(roles == null) {
				roles = new ArrayList<String>();
			}
			int i = userService.grant(userId, roles);
		} catch (Exception e) {
			throw new AdminGeneralException(
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getCode(),
					ExceptionEnum.HTTP_INTERNAL_SERVER_ERROR.getMessage(),
					e.getMessage());
		}
		return "redirect:/admin/user/toGrant?userId=" + userId;
	}
	
	@RequestMapping("toUpdatePwd")
	public ModelAndView toUpdatePwd(String userId) {
		ModelAndView mv = new ModelAndView();
		if(StringUtil.isBlank(userId)) {
			mv.addObject("user", TokenManager.getToken());
		}else {
			User user = userService.getByID(userId);
			mv.addObject("user", user);
		}
		mv.setViewName("admin/user/password");
		return mv;
	}
	
	/**
	 * 修改密码
	 * @param user
	 * @return
	 */
	@RequestMapping("/updatePwd")
	@ResponseBody
	public ResponseResult updatePwd(@RequestBody UserPasswordVO user) {
		User user2 = userService.getByID(user.getId());
		ByteSource oldSalt = ByteSource.Util.bytes(user2.getUsername() + user.getOldPwd());
		SimpleHash sh = new SimpleHash("MD5", user.getOldPwd(), oldSalt, 1024);
		
		if(!user2.getPassword().equals(sh.toString())) {
			return ResponseResult.Factory.newInstance(Boolean.FALSE, StateCodeEnum.USER_OLDPWD_ERROR.getCode(), null, StateCodeEnum.USER_OLDPWD_ERROR.getDescription());
		}
		
		ByteSource newSalt = ByteSource.Util.bytes(user2.getUsername() + user.getNewPwd());
		SimpleHash sh1 = new SimpleHash("MD5", user.getNewPwd(), newSalt, 1024);
		user2.setSalt(newSalt.toString());
		user2.setPassword(sh1.toString());
		userService.save(user2);
		return ResponseResult.Factory.newInstance(Boolean.TRUE, StateCodeEnum.HTTP_SUCCESS.getCode(), null, StateCodeEnum.HTTP_SUCCESS.getDescription());
	}
	
}
