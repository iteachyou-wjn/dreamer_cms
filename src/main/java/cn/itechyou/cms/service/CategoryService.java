package cn.itechyou.cms.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Category;
import cn.itechyou.cms.exception.CmsException;

public interface CategoryService {

	void save(Category category);

	PageInfo<Category> queryListByPage(SearchEntity params);

	Category selectById(String id);

	List<Category> selectByParentId(String id);

	int update(Category category);

	int delete(String id) throws CmsException;

	Category queryCategoryByCode(String code);

	void updateSort(List<Category> list);

	List<Category> getTreeList(Map<String, Object> params);
	
	List<Category> queryListByCode(Map<String, Object> entity);

	List<Category> queryAll();

}
