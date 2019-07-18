package cn.itechyou.blog.controller.admin;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.common.ResponseResult;
import cn.itechyou.blog.common.SearchEntity;
import cn.itechyou.blog.common.StateCodeEnum;
import cn.itechyou.blog.entity.User;
import cn.itechyou.blog.service.UserService;
import cn.itechyou.blog.utils.LoggerUtils;

/**
 * 用户管理
 * @author 王俊南
 * @date 2018-07-30
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {
	private Logger logger = LoggerUtils.getPlatformLogger();
	
	@Resource
	private UserService userService;
	
	@RequestMapping("index")
	public ModelAndView index() {
		logger.info(">>>>>>>>>>进入用户管理index方法<<<<<<<<<<");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/user/list");
		return mv;
	}
	
	@RequestMapping("/list")
	public ResponseResult list(@RequestBody SearchEntity page) {
		logger.info(">>>>>>>>>>进入用户管理list方法<<<<<<<<<<");
		if(page.getPageNum() >= 1) {
			page.setPageNum(page.getPageNum() - 1);
		}
		if(page.getPageSize() == 0) {
			page.setPageSize(10);
		}
		PageInfo<User> list = userService.listByPage(page);
		return ResponseResult.Factory.newInstance(Boolean.TRUE, StateCodeEnum.HTTP_SUCCESS.getCode(), list, StateCodeEnum.HTTP_SUCCESS.getDescription());
	}
	
	@RequestMapping("/save")
	public ResponseResult save(@RequestBody User user) {
		logger.info(">>>>>>>>>>进入用户管理save方法<<<<<<<<<<");
		ResponseResult result = null;
		if(user.getId() == null) {
			user.setCreateTime(new Date());
		}else {
			user.setUpdateTime(new Date());
		}
		result = this.userService.save(user);
		return result;
	}
	
	@RequestMapping("/remove/{id}")
	public ResponseResult remove(@PathVariable("id") String id) {
		logger.info(">>>>>>>>>>进入用户管理remove方法<<<<<<<<<<");
		if(id == null || "".equals(id)) {
			return ResponseResult.Factory.newInstance(Boolean.FALSE, StateCodeEnum.ARGUMENTERROR.getCode(), null, StateCodeEnum.ARGUMENTERROR.getDescription());
		}
		ResponseResult result = this.userService.remove(id);
		return result;
	}
	
	@RequestMapping("/get/{id}")
	public ResponseResult get(@PathVariable("id") String id) {
		logger.info(">>>>>>>>>>进入用户管理get方法<<<<<<<<<<");
		if(id == null || "".equals(id)) {
			return ResponseResult.Factory.newInstance(Boolean.FALSE, StateCodeEnum.ARGUMENTERROR.getCode(), null, StateCodeEnum.ARGUMENTERROR.getDescription());
		}
		ResponseResult result = this.userService.getByID(id);
		return result;
	}
}
