package cc.iteachyou.cms.controller.admin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.annotation.Log.OperatorType;
import cc.iteachyou.cms.common.BaseController;
import cc.iteachyou.cms.common.Constant;
import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.entity.System;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.exception.XssAndSqlException;
import cc.iteachyou.cms.service.SystemService;

/**
 * 系统设置
 * @author Wangjn
 *
 */
@Controller
@RequestMapping("admin/system")
public class SystemController extends BaseController {
	@Autowired
	private SystemService systemService;
	
	/**
	 * 首页跳转
	 * @return
	 */
	@Log(operType = OperatorType.SELECT, module = "系统设置", content = "查询系统设置")
	@RequestMapping({"","toIndex"})
	@RequiresPermissions("system:setting:update")
	public String toIndex(Model model) {
		System system = systemService.getSystem();
		model.addAttribute("system", system);
		return "admin/system/system";
	}
	
	/**
	 * 更新
	 * @return
	 * @throws CmsException 
	 */
	@Log(operType = OperatorType.UPDATE, module = "系统设置", content = "修改系统设置")
	@RequestMapping("update")
	@RequiresPermissions("system:setting:update")
	public String update(System system) throws CmsException {
		Pattern pattern = Pattern.compile(Constant.FILE_NAME_REGEXP);
		Matcher uploadDirMatcher = pattern.matcher(system.getUploaddir());
		if(uploadDirMatcher.find()) {
			throw new XssAndSqlException(
					ExceptionEnum.XSS_SQL_EXCEPTION.getCode(),
					ExceptionEnum.XSS_SQL_EXCEPTION.getMessage(),
					"默认上传目录名疑似不安全，详情：" + system.getUploaddir());
		}
		Matcher staticDirmatcher = pattern.matcher(system.getStaticdir());
		if(staticDirmatcher.find()) {
			throw new XssAndSqlException(
					ExceptionEnum.XSS_SQL_EXCEPTION.getCode(),
					ExceptionEnum.XSS_SQL_EXCEPTION.getMessage(),
					"默认静态化目录名疑似不安全，详情：" + system.getStaticdir());
		}
		int num = systemService.update(system);
		return "redirect:/admin/system/toIndex";
	}
}
