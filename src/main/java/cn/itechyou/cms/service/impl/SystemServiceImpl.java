package cn.itechyou.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itechyou.cms.dao.SystemMapper;
import cn.itechyou.cms.entity.System;
import cn.itechyou.cms.service.SystemService;
import cn.itechyou.cms.utils.FileConfiguration;

/**
 * 系统设置业务处理类
 * 
 * @author LIGW
 */
@Service
public class SystemServiceImpl implements SystemService {
	@Autowired
	private SystemMapper systemMapper;
	@Autowired
	private FileConfiguration fileConfiguration;

	/**
	 * 系统设置列表
	 */
	@Override
	public System getSystem() {
		System System = systemMapper.getCurrentSystem();
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
