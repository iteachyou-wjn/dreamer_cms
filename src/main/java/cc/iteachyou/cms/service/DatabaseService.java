package cc.iteachyou.cms.service;

import java.util.List;

public interface DatabaseService {

	List<String> showTables();

	int backup(String tableName);

	String showStruct(String tableName);

	int restore(String[] fileNames);

}
