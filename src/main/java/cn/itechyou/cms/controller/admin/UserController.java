package cn.itechyou.cms.controller.admin;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.ResponseResult;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.common.StateCodeEnum;
import cn.itechyou.cms.entity.User;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.UserService;
import cn.itechyou.cms.utils.LoggerUtils;
import cn.itechyou.cms.vo.UserPasswordVO;

/**
 * 用户管理
 * @author 王俊南
 * @date 2018-07-30
 *
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {
	private Logger logger = LoggerUtils.getPlatformLogger();
	
	@Resource
	private UserService userService;
	
	@RequestMapping("toUpdatePwd")
	public ModelAndView toUpdatePwd() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("user", TokenManager.getToken());
		mv.setViewName("admin/user/password");
		return mv;
	}
	
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
		return ResponseResult.Factory.newInstance(Boolean.FALSE, StateCodeEnum.HTTP_SUCCESS.getCode(), null, StateCodeEnum.HTTP_SUCCESS.getDescription());
	}
	
}
