package cc.iteachyou.cms.dao;

import java.util.Map;

public interface DashboardMapper {

	Map<String, Integer> statistics();

	String selectDBVersion();

}
