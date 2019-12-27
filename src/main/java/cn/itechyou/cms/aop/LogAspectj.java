package cn.itechyou.cms.aop;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.itechyou.cms.annotation.Log;
import cn.itechyou.cms.entity.SysLogger;
import cn.itechyou.cms.security.token.TokenManager;
import cn.itechyou.cms.service.LoggerService;
import cn.itechyou.cms.utils.RequestEntity;
import cn.itechyou.cms.utils.UUIDUtils;

/**
 * 日志记录切面
 * 扫描所有带Log注解的Controller
 * Created by Wangjn
 */
@Aspect
@Component
public class LogAspectj {
	@Autowired
	private LoggerService loggerService;

	@Pointcut("@annotation(cn.itechyou.cms.annotation.Log)")
	public void logPointCut() {

	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		// 执行方法
		Object result = point.proceed();
		// 执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		// 保存日志
		saveSysLog(point, time);

		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLogger logEntity = new SysLogger();
		logEntity.setId(UUIDUtils.getPrimaryKey());
		logEntity.setLevel("INFO");
		logEntity.setOperType(1);
		
		Log syslog = method.getAnnotation(Log.class);
		if (syslog != null) {
			// 注解上的描述
			logEntity.setModule(syslog.module());
			logEntity.setContent(syslog.content());
		}

		// 请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		logEntity.setOperSource(className + "." + methodName + "()");
		
		// 请求的参数
		/*Object[] args = joinPoint.getArgs();
		try {
			String params = new Gson().toJson(args[0]);
			//logEntity.setExtend1(params);
		} catch (Exception e) {

		}*/
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		// 获取request
	    HttpServletRequest request = attr == null ? null : attr.getRequest();
	    RequestEntity webRequest = RequestEntity.fromWebRequest(request);
		// 设置IP地址
		logEntity.setIp(webRequest.getRemoteAddr());
		logEntity.setBrowser(webRequest.getBroswer());
		logEntity.setPlatform(webRequest.getPlatform());

		// 用户信息
		String userid = TokenManager.getToken() == null ? SysLogger.UNKNOW : TokenManager.getToken().getId();
		String username = TokenManager.getToken() == null ? SysLogger.UNKNOW : TokenManager.getToken().getUsername();
		logEntity.setOperUser(username);
		logEntity.setCreateBy(userid);
		logEntity.setCreateTime(new Date());
		// 保存系统日志
		loggerService.insert(logEntity);
	}
}
