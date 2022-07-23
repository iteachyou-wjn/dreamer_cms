package cc.iteachyou.cms.exception;

public class TemplatePermissionDeniedException extends CmsException{

	private static final long serialVersionUID = 3969404221975913175L;

	public TemplatePermissionDeniedException(String code, String message, String reason) {
		super(code, message, reason);
	}
	
}
