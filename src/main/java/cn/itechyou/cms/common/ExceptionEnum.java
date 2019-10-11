package cn.itechyou.cms.common;

public enum ExceptionEnum {
	HTTP_BAD_REQUEST("400","错误的请求"),
	HTTP_UNAUTHORIZED("401","未经授权"),
	HTTP_FORBIDDEN("403","禁止访问"),
	HTTP_NOT_FOUND("404","资源没有找到"),
	HTTP_METHOD_NOT_ALLOWED("405","方法不允许"),
	HTTP_UNSUPPORTED_MEDIA_TYPE("415","不支持的媒体类型"),
	HTTP_INTERNAL_SERVER_ERROR("500","内部服务器错误"),
	
	
	TEMPLATE_NOTFOUND_EXCEPTION("40001","模版文件不存在"),
	TEMPLATE_READ_EXCEPTION("40002","模版文件读取失败"),
	;
	private String code;
	private String message;
	
	private ExceptionEnum(String code, String message) {
		this.code = code;
		this.message = message;
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

}
