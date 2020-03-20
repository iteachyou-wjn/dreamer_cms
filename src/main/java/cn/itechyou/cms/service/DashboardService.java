package cn.itechyou.cms.service;

import java.util.Map;

public interface DashboardService {

	Map<String,Integer> statistics();

	String selectDBVersion();

}
