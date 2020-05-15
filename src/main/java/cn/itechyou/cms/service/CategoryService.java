package cn.itechyou.cms.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.Category;
import cn.itechyou.cms.entity.CategoryWithBLOBs;

public interface CategoryService {

	void save(CategoryWithBLOBs category);

	PageInfo<CategoryWithBLOBs> queryListByPage(SearchEntity params);

	CategoryWithBLOBs selectById(String id);

	List<CategoryWithBLOBs> selectByParentId(String id);

	int update(CategoryWithBLOBs category);

	int delete(String id);

	CategoryWithBLOBs queryCategoryByCode(String code);

	void updateSort(List<Category> list);

	List<CategoryWithBLOBs> getTreeList(String parentId);
	
	List<CategoryWithBLOBs> getTreeList(String parentId,String isShow);

	List<CategoryWithBLOBs> queryListByCode(Map<String, Object> entity);

	List<CategoryWithBLOBs> queryAll();

}
