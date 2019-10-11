package cn.itechyou.cms.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.itechyou.cms.common.LogEnum;

/**
 * 本地日志参考类
 * 
 * @author Administrator
 *
 */
public class LoggerUtils {

	/**
	 * 获取业务日志logger
	 *
	 * @return
	 */
	public static Logger getBussinessLogger() {
		return LoggerFactory.getLogger(LogEnum.BUSSINESS.getCategory());
	}

	/**
	 * 获取平台日志logger
	 *
	 * @return
	 */
	public static Logger getPlatformLogger() {
		return LoggerFactory.getLogger(LogEnum.PLATFORM.getCategory());
	}

	/**
	 * 获取数据库日志logger
	 *
	 * @return
	 */
	public static Logger getDBLogger() {
		return LoggerFactory.getLogger(LogEnum.DATABASE.getCategory());
	}

	/**
	 * 获取异常日志logger
	 *
	 * @return
	 */
	public static Logger getExceptionLogger() {
		return LoggerFactory.getLogger(LogEnum.EXCEPTION.getCategory());
	}

	public static Logger getLogger(Class<?> class1) {
		return LoggerFactory.getLogger(class1);
	}

}
