package cc.iteachyou.cms.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.Menu;

public interface MenuService {

	PageInfo<Menu> queryListByPage(SearchEntity params);

	int add(Menu menu);

	Menu queryMenuById(String id);

	int update(Menu menu);

	int delete(String id);

	List<Menu> queryAll();

	List<Menu> queryListByUserId(String userId);

}
