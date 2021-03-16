package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import cn.itechyou.cms.common.BaseMapper;
import cn.itechyou.cms.entity.Scheduled;

/**
 * ScheduledMapper继承基类
 */
public interface ScheduledMapper extends BaseMapper<Scheduled> {

	List<Scheduled> queryListByPage(Map<String, Object> entity);

	Scheduled selectOneByClassName(String name);
}