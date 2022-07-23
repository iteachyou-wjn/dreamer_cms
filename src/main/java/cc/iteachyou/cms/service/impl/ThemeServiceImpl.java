package cc.iteachyou.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.dao.ThemeMapper;
import cc.iteachyou.cms.entity.Theme;
import cc.iteachyou.cms.service.ThemeService;

/**
 * 主题
 * @author 王俊南
 *
 */
@Service
public class ThemeServiceImpl implements ThemeService {
	
	@Autowired
	private ThemeMapper themeMapper;

	@Override
	public List<Theme> queryListByPage(SearchEntity params) {
		List<Theme> list = themeMapper.queryListByPage(params.getEntity());
		return list;
	}

	@Override
	public int save(Theme theme) {
		return themeMapper.insertSelective(theme);
	}

	@Override
	public int update(Theme theme) {
		return themeMapper.updateByPrimaryKeySelective(theme);
	}

	@Override
	public int batchUpdateStatus(Map<String,Object> params) {
		return themeMapper.batchUpdateStatus(params);
	}

	@Override
	public int delete(String id) {
		return themeMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<Theme> queryByPathName(String themePath) {
		return themeMapper.queryByPathName(themePath);
	}

	@Override
	public Theme getCurrentTheme() {
		return themeMapper.getCurrentTheme();
	}

}
