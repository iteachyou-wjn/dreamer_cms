package cn.itechyou.cms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 消息
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
@AllArgsConstructor
public class Message {
	private String code;
	private String message;
	private Integer progress;
}
