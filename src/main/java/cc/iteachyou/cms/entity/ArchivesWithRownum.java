package cc.iteachyou.cms.entity;

import lombok.Data;

/**
 * 文章扩展
 * @author 王俊南
 * Date: 2020-12-29
 */
@Data
public class ArchivesWithRownum extends Archives {
	private Integer rownum;
}
