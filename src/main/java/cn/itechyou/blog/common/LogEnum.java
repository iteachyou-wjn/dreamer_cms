package cn.itechyou.blog.common;

/**
 * 本地日志枚举
 * 
 */
public enum LogEnum {

	BUSSINESS("bussiness"),

	PLATFORM("platform"),

	DB("db"),

	EXCEPTION("exception"),

	;

	private String category;

	LogEnum(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
