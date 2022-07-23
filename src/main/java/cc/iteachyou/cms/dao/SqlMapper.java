package cc.iteachyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * SqlMapper继承基类
 */
public interface SqlMapper {
	List<Map<String,Object>> execute(@Param("sql") String sql);
}