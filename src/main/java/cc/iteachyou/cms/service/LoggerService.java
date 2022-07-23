package cc.iteachyou.cms.service;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpSession;

import cc.iteachyou.cms.annotation.Log;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.SysLogger;
import cc.iteachyou.cms.utils.RequestEntity;

public interface LoggerService {
	void log(Method method, Log log, RequestEntity request, HttpSession session);

	void info(String module, String msg);

	void error(String module, String msg);

	int insert(SysLogger logEntity);
	
	List<SysLogger> getList(SearchEntity searchEntity);
}
