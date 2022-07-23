package cc.iteachyou.cms.service;

import java.util.Map;

public interface DashboardService {

	Map<String,Integer> statistics();

	String selectDBVersion();

}
