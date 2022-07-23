package cc.iteachyou.cms.exception;

public class CategoryNotFoundException extends CmsException{

	private static final long serialVersionUID = 3969404221975913175L;

	public CategoryNotFoundException(String code, String message, String reason) {
		super(code, message, reason);
	}
	
}
