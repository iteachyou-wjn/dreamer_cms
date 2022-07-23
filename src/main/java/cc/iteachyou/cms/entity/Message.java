package cc.iteachyou.cms.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 消息
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
@AllArgsConstructor
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String message;
	private Integer progress;
}
