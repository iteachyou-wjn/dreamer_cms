package cn.itechyou.cms.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import cn.itechyou.cms.dao.SystemMapper;
import cn.itechyou.cms.entity.System;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.utils.FileConfiguration;
import cn.itechyou.cms.utils.LoggerUtils;

/**
 * 系统设置业务处理类
 * 
 * @author LIGW
 */
@Service
public class SystemServiceImpl implements SystemService {

	/**
	 * 日志输出
	 */
	private static final Logger logger = LoggerUtils.getLogger(SystemServiceImpl.class);
	@Autowired
	private SystemMapper systemMapper;
	@Autowired
	private FileConfiguration fileConfiguration;

	/**
	 * 系统设置列表
	 */
	@Override
	public System getSystem() {
		System System = systemMapper.selectAll();
		return System;
	}

	/**
	 * 更新系统设置
	 */
	@Override
	public int update(System system) {
		int num = systemMapper.updateByPrimaryKey(system);
		return num;
	}

}
