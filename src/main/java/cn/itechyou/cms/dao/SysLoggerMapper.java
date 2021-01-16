package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import cn.itechyou.cms.common.BaseMapper;
import cn.itechyou.cms.entity.SysLogger;

public interface SysLoggerMapper extends BaseMapper<SysLogger> {

	List<SysLogger> queryListByPage(Map<String, Object> entity);
}