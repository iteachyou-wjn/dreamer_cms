package cc.iteachyou.cms.common;

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
	FORM_PARAMETER_EXCEPTION("40003","表单参数错误"),
	FILE_NOTFOUND_EXCEPTION("40004", "文件不存在"),
	FILE_FORMAT_ERROR_EXCEPTION("40005", "文件格式错误"),
	CAT_NOTFOUND_EXCEPTION("40006", "栏目不存在"),
	TEMPLATE_PARSE_EXCEPTION("40007", "模版文件解析错误"),
	LICENCE_PARAMETER_EXCEPTION("40008", "授权失败"),
	USERNAME_EXIST_EXCEPTION("40009", "用户名已存在"),
	TASK_CLASSNOTFOUND_EXCEPTION("40010", "定时任务类不存在"),
	CATEGORY_REMOVE_EXCEPTION("40011", "栏目存在下级栏目，删除失败"),
	VARIABLE_EXIST_EXCEPTION("40012", "变量名称已存在"),
	XSS_SQL_EXCEPTION("40013", "您的请求中有违反安全规则的元素"),
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
