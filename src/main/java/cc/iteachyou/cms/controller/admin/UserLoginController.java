package cc.iteachyou.cms.controller.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.utils.CaptchaUtil;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.common.BaseController;
import cc.iteachyou.cms.common.ResponseResult;
import cc.iteachyou.cms.common.StateCodeEnum;
import cc.iteachyou.cms.entity.Menu;
import cc.iteachyou.cms.entity.User;
import cc.iteachyou.cms.security.token.TokenManager;
import cc.iteachyou.cms.service.MenuService;
import cc.iteachyou.cms.vo.UserVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户登录相关，不需要做登录限制
 * 
 */
@Slf4j
@Controller
@RequestMapping("/admin/u")
public class UserLoginController extends BaseController {
	@Autowired
	private MenuService menuService;
	

	// 产生验证码
	@RequestMapping("/getVerifyCode")
	public void getKaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
        captcha.text();  // 获取运算的结果：5
		CaptchaUtil.out(captcha, request, response);
	}

	/**
	 * 登录跳转
	 * 
	 * @return
	 */
	@RequestMapping("toLogin")
	public ModelAndView toLogin() {
		ModelAndView mv = new ModelAndView();
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		mv.setViewName("admin/login");
		return mv;
	}
	
	/**
	 * 首页跳转
	 * 
	 * @return
	 */
	@RequestMapping("toIndex")
	public ModelAndView toIndex() {
		ModelAndView mv = new ModelAndView();
		String userId = TokenManager.getUserId();
		/**
		 * 查询当前用户所拥有的菜单权限
		 */
		List<Menu> menus = menuService.queryListByUserId(userId);
		mv.addObject("menus", menus);
		mv.setViewName("admin/index");
		return mv;
	}

	/**
	 * 登录提交
	 * @param entity 登录的UUser
	 * @param rememberMe 是否记住
	 * @param request，用来取登录之前Url地址，用来登录后跳转到没有登录之前的页面。
	 * @return
	 */
	@Log(operType = OperatorType.OTHER, module = "登录模块", content = "用户登录")
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult login(@RequestBody UserVO entity,HttpServletRequest request) {
		ResponseResult result = null;
		User user = new User();
		try {
			// 验证码校验
			if(!CaptchaUtil.ver(entity.getVcode(), request)) {
				result = ResponseResult.Factory.newInstance(Boolean.FALSE,
						StateCodeEnum.USER_CODE_ERROR.getCode(), null,
						StateCodeEnum.USER_CODE_ERROR.getDescription());
				return result;
			}
			
			boolean rememberMe = entity.isRememberMe();
			ByteSource salt = ByteSource.Util.bytes(entity.getUsername() + entity.getPassword());
			SimpleHash sh = new SimpleHash("MD5", entity.getPassword(), salt, 1024);
			user.setUsername(entity.getUsername());
			user.setPassword(sh.toString());
			user.setSaltByte(salt);
			user = TokenManager.login(user, rememberMe, salt);

			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("users", user);
			result = ResponseResult.Factory.newInstance(Boolean.TRUE,
					StateCodeEnum.HTTP_SUCCESS.getCode(), user,
					StateCodeEnum.HTTP_SUCCESS.getDescription());
		} catch (DisabledAccountException e) {
			// 帐号已经禁用
			result = ResponseResult.Factory.newInstance(Boolean.FALSE,
					StateCodeEnum.USER_MOBILE_EXCEPTION.getCode(), null,
					StateCodeEnum.USER_MOBILE_EXCEPTION.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
			// 帐号或密码错误
			result = ResponseResult.Factory.newInstance(Boolean.FALSE,
					StateCodeEnum.USER_PASSWORD_ERROR.getCode(), null,
					StateCodeEnum.USER_PASSWORD_ERROR.getDescription());
		}
		return result;
	}

	/**
	 * 退出
	 * 
	 * @return
	 */
	@Log(operType = OperatorType.OTHER, module = "登录模块", content = "用户退出登录")
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		try {
			TokenManager.logout();
		} catch (Exception e) {
			log.error("errorMessage:" + e.getMessage());
		}
		return "redirect:/admin/toLogin";
	}
}
