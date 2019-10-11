package cn.itechyou.cms.exception;

public class CmsException extends Exception {
	
	private static final long serialVersionUID = 2415656102753230136L;
	
	private String code;
	private String message;
	private String reason;

	public CmsException() {
		super();
	}

	public CmsException(String code, String message, String reason) {
		super(message);
		this.code = code;
		this.message = message;
		this.reason = reason;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
