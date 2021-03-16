package cn.itechyou.cms.controller.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.hutool.captcha.CircleCaptcha;
import cn.itechyou.cms.annotation.Log;
import cn.itechyou.cms.common.BaseController;
import cn.itechyou.cms.common.Constant;
import cn.itechyou.cms.common.ResponseResult;
import cn.itechyou.cms.common.StateCodeEnum;
import cn.itechyou.cms.entity.Licence;
import cn.itechyou.cms.entity.Menu;
import cn.itechyou.cms.entity.User;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.LicenceService;
import cn.itechyou.cms.service.MenuService;
import cn.itechyou.cms.service.RoleService;
import cn.itechyou.cms.utils.LoggerUtils;
import cn.itechyou.cms.vo.UserVO;

/**
 * 用户登录相关，不需要做登录限制
 * 
 */
@Controller
@RequestMapping("/admin/u")
public class UserLoginController extends BaseController {

	private static final Logger logger = LoggerUtils.getLogger(UserLoginController.class);
	protected Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
	public static String URL404 = "/404.html";

	@Autowired
	private CircleCaptcha captcha;
	@Autowired
	private LicenceService licenceService;
	@Autowired
	private MenuService menuService;
	

	// 产生验证码
	@RequestMapping("/getVerifyCode")
	public void getKaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		captcha.createCode();
		//图形验证码写出，可以写出到文件，也可以写出到流
		String text = captcha.getCode();
		//验证图形验证码的有效性，返回boolean值
		logger.info("生成的验证码为：" + text);
		session.setAttribute(Constant.KAPTCHA, text);
		// 形成一张图片
		// 把图片写入到输出流中==》以流的方式响应到客户端
		OutputStream outputStream = response.getOutputStream();
		captcha.write(outputStream);
		outputStream.close();
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
		Licence licence = licenceService.getLicence();
		/**
		 * 查询当前用户所拥有的菜单权限
		 */
		List<Menu> menus = menuService.queryListByUserId(userId);
		mv.addObject("menus", menus);
		mv.addObject("licence", licence);
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
	@Log(module = "登录模块",content = "用户登录操作")
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult login(@RequestBody UserVO entity,HttpServletRequest request) {
		ResponseResult result = null;
		User user = new User();
		try {
			// 验证码校验
			if(!captcha.verify(entity.getVcode())) {
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
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		try {
			TokenManager.logout();
		} catch (Exception e) {
			logger.error("errorMessage:" + e.getMessage());
		}
		return "redirect:/admin/toLogin";
	}
}
