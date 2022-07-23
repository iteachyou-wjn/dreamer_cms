package cc.iteachyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * SqlMapper继承基类
 */
public interface DatabaseMapper {
	String selectDatabase();
	
	List<String> showTables();

	Map<String,String> showTableStruct(@Param("tableName") String tableName);

	List<Map<String, Object>> showTableColumns(String database, String tableName);

	List<Map<String, Object>> showAllDatas(String database, String tableName);

}