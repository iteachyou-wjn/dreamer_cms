package cn.itechyou.cms.controller.admin;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.ExceptionEnum;
import cn.itechyou.cms.common.ResponseResult;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.common.StateCodeEnum;
import cn.itechyou.cms.entity.User;
import cn.itechyou.cms.exception.AdminGeneralException;
import cn.itechyou.cms.exception.CmsException;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.UserService;
import cn.itechyou.cms.utils.LoggerUtils;
import cn.itechyou.cms.utils.StringUtil;
import cn.itechyou.cms.utils.UUIDUtils;
import cn.itechyou.cms.vo.UserPasswordVO;

/**
 * 用户管理
 * @author 王俊南
 * @date 2018-07-30
 *
 */
@Controller
@RequestMapping("/admin/user")
public class UserController {
	private Logger logger = LoggerUtils.getPlatformLogger();
	
	@Resource
	private UserService userService;
	
	@RequestMapping("list")
	public ModelAndView list(Model model, SearchEntity searchEntity) {
		ModelAndView mv = new ModelAndView();
		PageInfo<User> pageInfo = userService.listByPage(searchEntity);
		mv.addObject("pageInfo", pageInfo);
		mv.setViewName("admin/user/list");
		return mv;
	}
	
	@RequestMapping("toAdd")
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
	public String delete(Model model, String id) {
		userService.deleteUser(id);
		return "redirect:/admin/user/list";
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
