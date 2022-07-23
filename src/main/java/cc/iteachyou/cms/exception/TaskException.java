package cc.iteachyou.cms.exception;

public class TaskException extends CmsException{

	private static final long serialVersionUID = 3969404221975913175L;

	public TaskException(String code, String message, String reason) {
		super(code, message, reason);
	}
	
}
