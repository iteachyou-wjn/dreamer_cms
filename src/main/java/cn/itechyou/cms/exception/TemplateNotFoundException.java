package cn.itechyou.cms.exception;

import cn.itechyou.cms.common.ExceptionEnum;

public class TemplateNotFoundException extends CmsException{

	private static final long serialVersionUID = 3969404221975913175L;

	public TemplateNotFoundException(String code, String message, String reason) {
		super(code, message, reason);
	}
	
	public TemplateNotFoundException(ExceptionEnum exceptionEnum, String reason) {
	    super(exceptionEnum.getCode(), exceptionEnum.getMessage(), reason);
	}
	
}
