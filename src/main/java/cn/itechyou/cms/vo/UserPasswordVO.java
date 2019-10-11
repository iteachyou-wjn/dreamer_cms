package cn.itechyou.cms.vo;

public class UserPasswordVO {
	private String id;
	private String oldPwd;
	private String newPwd;
	private String reNewPwd;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public String isReNewPwd() {
		return reNewPwd;
	}

	public void setReNewPwd(String reNewPwd) {
		this.reNewPwd = reNewPwd;
	}

}
