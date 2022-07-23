package cc.iteachyou.cms.service;

import java.util.List;
import java.util.Map;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.Theme;

public interface ThemeService {

	List<Theme> queryListByPage(SearchEntity params);

	int save(Theme theme);

	int update(Theme theme);

	int batchUpdateStatus(Map<String,Object> params);

	int delete(String id);

	List<Theme> queryByPathName(String themePath);

	Theme getCurrentTheme();

}
