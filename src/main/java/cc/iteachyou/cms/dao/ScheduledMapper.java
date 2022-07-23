package cc.iteachyou.cms.dao;

import java.util.List;
import java.util.Map;

import cc.iteachyou.cms.common.BaseMapper;
import cc.iteachyou.cms.entity.Scheduled;

/**
 * ScheduledMapper继承基类
 */
public interface ScheduledMapper extends BaseMapper<Scheduled> {

	List<Scheduled> queryListByPage(Map<String, Object> entity);

	Scheduled selectOneByClassName(String name);
}