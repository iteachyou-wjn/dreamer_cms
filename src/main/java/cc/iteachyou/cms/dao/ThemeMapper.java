package cc.iteachyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cc.iteachyou.cms.common.BaseMapper;
import cc.iteachyou.cms.entity.Theme;

public interface ThemeMapper extends BaseMapper<Theme> {

	List<Theme> queryListByPage(Map<String, Object> entity);

	int batchUpdateStatus(Map<String, Object> params);

	List<Theme> queryByPathName(@Param("themePath") String themePath);

	Theme getCurrentTheme();

}