package cn.itechyou.cms.entity;

public class Message {
	private String code;
	private String message;
	private Integer progress;
	
	public Message(String code, String message, Integer progress) {
		super();
		this.code = code;
		this.message = message;
		this.progress = progress;
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

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

}
