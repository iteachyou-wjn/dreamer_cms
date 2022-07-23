package cc.iteachyou.cms.dao;

import java.util.List;
import java.util.Map;

import cc.iteachyou.cms.common.BaseMapper;
import cc.iteachyou.cms.entity.SysLogger;

public interface SysLoggerMapper extends BaseMapper<SysLogger> {

	List<SysLogger> queryListByPage(Map<String, Object> entity);
}