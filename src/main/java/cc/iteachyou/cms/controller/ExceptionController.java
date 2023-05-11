package cc.iteachyou.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.exception.XssAndSqlException;

@Controller
@RequestMapping("exception")
public class ExceptionController {
	
	@RequestMapping("")
	public void customerException() throws CmsException {
		throw new XssAndSqlException(
				ExceptionEnum.XSS_SQL_EXCEPTION.getCode(), 
				ExceptionEnum.XSS_SQL_EXCEPTION.getMessage(), 
				"您所访问的页面请求中有违反安全规则元素存在，拒绝访问。");
	}
	
	@RequestMapping("403")
	public void exception403() throws CmsException {
		throw new XssAndSqlException(
				ExceptionEnum.HTTP_FORBIDDEN.getCode(), 
				ExceptionEnum.HTTP_FORBIDDEN.getMessage(), 
				"拒绝访问。");
	}
}
