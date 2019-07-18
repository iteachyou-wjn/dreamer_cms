package cn.itechyou.blog.service;

import java.util.List;
import java.util.Map;

import cn.itechyou.blog.common.SearchEntity;
import cn.itechyou.blog.entity.Theme;

public interface ThemeService {

	List<Theme> queryListByPage(SearchEntity params);

	int save(Theme theme);

	int update(Theme theme);

	int batchUpdateStatus(Map<String,Object> params);

	int delete(String id);

	List<Theme> queryByPathName(String themePath);

	Theme getCurrentTheme();

}
