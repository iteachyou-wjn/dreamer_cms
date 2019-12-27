package cn.itechyou.cms.service.impl;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

import cn.itechyou.cms.annotation.Log;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.dao.SysLoggerMapper;
import cn.itechyou.cms.entity.SysLogger;
import cn.itechyou.cms.entity.User;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.LoggerService;
import cn.itechyou.cms.utils.HttpRequestUtils;
import cn.itechyou.cms.utils.RequestEntity;
import cn.itechyou.cms.utils.StringUtil;
import cn.itechyou.cms.utils.UUIDUtils;

/**
 * 记录日志到数据库中 数据库中
 * level字段值对应 
 * 1:info 2:debug 3:error
 * <p>
 * 日志存储到数据库为异步存储，防止由于日志存储导致服务器功能无法访问
 *
 * @author Wangjn
 */
@Service
@EnableAsync
public class LoggerServiceImpl implements LoggerService {
	@Autowired
	private SysLoggerMapper loggerMapper;
    
	/**
     * 记录日常日志
     *
     * @param module   模块名称
     * @param msg
     * @param username
     * @param request
     */
    @Async
    @Transactional
    public void info(String module, String msg, String username, RequestEntity request) {
    	SysLogger log = new SysLogger();
    	log.setId(UUIDUtils.getPrimaryKey());
        log.setLevel("INFO");
        log.setModule(module);
        log.setContent(msg);
        log.setOperUser(username);
        log.setOperType(1);
        log.setIp(request.getRemoteAddr());
        log.setBrowser(request.getBroswer());
        log.setPlatform(request.getPlatform());
        log.setCreateTime(new Date());
        log.setCreateBy(TokenManager.getToken() == null ? SysLogger.UNKNOW : TokenManager.getToken().getId());
        log.setOperSource(getLogClassName());
        loggerMapper.insertSelective(log);

        LoggerFactory.getLogger(log.getOperSource()).info(msg);
    }

    /**
     * 记录日常日志
     *
     * @param module 模块名称
     * @param msg
     */
    @Transactional
    @Override
    public void info(String module, String msg) {
        this.info(module, msg, TokenManager.getToken() == null ? SysLogger.UNKNOW : TokenManager.getToken().getUsername(), RequestEntity.fromWebRequest(HttpRequestUtils.getRequest()));
    }

    /**
     * 记录错误日志
     *
     * @param module
     * @param msg
     * @param username
     * @param request
     */
    @Async
    @Transactional
    public void error(String module, String msg, String username, RequestEntity request) {
    	SysLogger log = new SysLogger();
    	log.setId(UUIDUtils.getPrimaryKey());
        log.setLevel("ERROR");
        log.setOperUser(username);
        log.setOperType(2);
        log.setIp(request.getRemoteAddr());
        log.setBrowser(request.getBroswer());
        log.setPlatform(request.getPlatform());
        log.setContent(msg);
        log.setCreateTime(new Date());
        log.setCreateBy(TokenManager.getToken() == null ? SysLogger.UNKNOW : TokenManager.getToken().getId());
        log.setOperSource(getLogClassName());
        log.setModule(module);
        loggerMapper.insertSelective(log);

        LoggerFactory.getLogger(log.getOperSource()).error(msg);
    }

    /**
     * 记录错误日志
     *
     * @param module
     * @param msg
     */
    @Transactional
    @Override
    public void error(String module, String msg) {
        this.error(module, msg, TokenManager.getToken() == null ? SysLogger.UNKNOW : TokenManager.getToken().getUsername(), RequestEntity.fromWebRequest(HttpRequestUtils.getRequest()));
    }

    /**
     * 记录注解日志
     *
     * @param method 记录日志的方法
     */
    @Async
    @Transactional
    @Override
    public void log(Method method, Log log, RequestEntity request, HttpSession session) {
        String content = log.content();
        //记录到Logger日志系统
        if (log.target() == Log.Target.ALL || log.target() == Log.Target.LOGGER) {
            Logger logger = LoggerFactory.getLogger(method.getDeclaringClass());
            String printContent = log.module() + ":" + content;
            if (log.level() == Log.Level.DEBUG) logger.debug(printContent);
            else if (log.level() == Log.Level.INFO) logger.info(printContent);
        }

        //记录到数据库
        if (log.target() == Log.Target.ALL || log.target() == Log.Target.DB) {
            //用户用户名
        	String userid = SysLogger.UNKNOW;
            String username = SysLogger.UNKNOW;
            if (session != null) {
                User user = TokenManager.getToken();
                if (user != null) {
                	userid = user.getId();
                    username = user.getUsername();
                }
            }
            SysLogger logInfo = new SysLogger();
            if (log.level() == Log.Level.INFO){
                logInfo.setLevel("INFO");
                logInfo.setOperType(1);
            }else if (log.level() == Log.Level.ERROR){
            	logInfo.setLevel("ERROR");
            	logInfo.setOperType(2);
            }
            logInfo.setId(UUIDUtils.getPrimaryKey());
            logInfo.setContent(content);
            logInfo.setOperUser(username);
            logInfo.setIp(request.getRemoteAddr());
            logInfo.setBrowser(request.getBroswer());
            logInfo.setPlatform(request.getPlatform());
            logInfo.setCreateTime(new Date());
            logInfo.setCreateBy(userid);
            logInfo.setOperSource(method.getDeclaringClass().getSimpleName() +"."+method.getName());
            logInfo.setModule(log.module());
            loggerMapper.insertSelective(logInfo);
        }
    }

	@Override
	public int insert(SysLogger logEntity) {
		return this.loggerMapper.insertSelective(logEntity);
	}
	
	@Override
	public List<SysLogger> getList(SearchEntity searchEntity) {
		if(StringUtil.isBlank(searchEntity.getPageNum())){
			searchEntity.setPageNum(1);
		}
		if(StringUtil.isBlank(searchEntity.getPageSize())){
			searchEntity.setPageSize(10);
		}
		//分页回显
		PageHelper.startPage(searchEntity.getPageNum(), searchEntity.getPageSize());
		Map<String, Object> entity = searchEntity.getEntity();
		return loggerMapper.queryListByPage(entity);
	}
	
	/**
	 * 获取类名称
	 *
	 * @return
	 */
	private String getLogClassName() {
		Throwable ex = new Throwable();
		StackTraceElement[] stackElements = ex.getStackTrace();
		int index = 0;
		for (int i = 0; i < stackElements.length; i++) {
			StackTraceElement e = stackElements[i];
			if (e.isNativeMethod()) {
				index = i - 1;
				break;
			}
		}
		String classname = stackElements[index].getClassName();
		String methodName = stackElements[index].getMethodName();
		//classname = classname.substring(classname.lastIndexOf(".") + 1, classname.length());
		
		if (classname.indexOf("$$") > 0) {
			classname = classname.substring(0, classname.indexOf("$$"));
		}
		return classname + "." + methodName + "()";
	}
}
