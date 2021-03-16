package cn.itechyou.cms.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Menu;

public interface MenuService {

	PageInfo<Menu> queryListByPage(SearchEntity params);

	int add(Menu menu);

	Menu queryMenuById(String id);

	int update(Menu menu);

	int delete(String id);

	List<Menu> queryAll();

	List<Menu> queryListByUserId(String userId);

}
