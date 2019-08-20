package cn.itechyou.blog.exception;

public class CmsException extends Exception {
	private String code;
	private String message;
	
	public CmsException() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CmsException(String code,String message) {
		super(message);
		this.code = code;
		this.message = message;
	}
	
	public CmsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}
	public CmsException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	public CmsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	public CmsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	
}
