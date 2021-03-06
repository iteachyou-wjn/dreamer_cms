package cn.itechyou.cms.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 许可
 * @author 王俊南
 * Date: 2021-3-5
 */
@Data
@Table(name = "system_licence")
public class Licence {
	@Id
	@Column(name = "id")
    private String id;

	/**
	 * 类型（1：个人；2：公司；3：政府；4：其它）
	 */
	@Column(name = "type")
    private String type;
	
	@Column(name = "main_name")
    private String mainName;
	
	@Column(name = "credit_code")
    private String creditCode;
	
	@Column(name = "licence_num")
	private String licenceNum;
	
	@Column(name = "main_domain")
    private String mainDomain;
	
	@Column(name = "encrypt_data")
    private String encryptData;
	
	@Column(name = "private_key")
    private String privateKey;
	
	@Column(name = "create_by")
    private String createBy;
	
	@Column(name = "create_time")
    private Date createTime;
	
	@Column(name = "update_by")
    private String updateBy;
	
	@Column(name = "update_time")
    private Date updateTime;
}
