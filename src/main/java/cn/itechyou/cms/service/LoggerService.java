package cn.itechyou.cms.service;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpSession;

import cn.itechyou.cms.annotation.Log;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.SysLogger;
import cn.itechyou.cms.utils.RequestEntity;

public interface LoggerService {
	void log(Method method, Log log, RequestEntity request, HttpSession session);

	void info(String module, String msg);

	void error(String module, String msg);

	int insert(SysLogger logEntity);
	
	List<SysLogger> getList(SearchEntity searchEntity);
}
