package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.itechyou.cms.entity.Theme;

public interface ThemeMapper {
    int deleteByPrimaryKey(String id);

    int insert(Theme record);

    int insertSelective(Theme record);

    Theme selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Theme record);

    int updateByPrimaryKey(Theme record);

	List<Theme> queryListByPage(Map<String, Object> entity);

	int batchUpdateStatus(Map<String, Object> params);

	List<Theme> queryByPathName(@Param("themePath") String themePath);

	Theme getCurrentTheme();

}