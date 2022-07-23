package cc.iteachyou.cms.exception;

public class FileFormatErrorException extends CmsException{

	private static final long serialVersionUID = 3969404221975913175L;

	public FileFormatErrorException(String code, String message, String reason) {
		super(code, message, reason);
	}
	
}
