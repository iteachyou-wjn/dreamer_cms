package cc.iteachyou.cms.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.iteachyou.cms.dao.DashboardMapper;
import cc.iteachyou.cms.service.DashboardService;

/**
 * 仪表盘
 * @author Wangjn
 *
 */
@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private DashboardMapper dashboardMapper;
	
	@Override
	public Map<String,Integer> statistics() {
		Map<String,Integer> result = dashboardMapper.statistics();
		return result;
	}

	@Override
	public String selectDBVersion() {
		return dashboardMapper.selectDBVersion();
	}

}
