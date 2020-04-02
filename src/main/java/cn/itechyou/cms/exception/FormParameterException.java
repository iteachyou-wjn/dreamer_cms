package cn.itechyou.cms.exception;

public class FormParameterException extends CmsException{

	private static final long serialVersionUID = 3969404221975913175L;

	public FormParameterException(String code, String message, String reason) {
		super(code, message, reason);
	}
	
}
