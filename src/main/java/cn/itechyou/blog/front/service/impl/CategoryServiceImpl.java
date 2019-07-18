package cn.itechyou.blog.front.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itechyou.blog.dao.CategoryMapper;
import cn.itechyou.blog.entity.CategoryWithBLOBs;
import cn.itechyou.blog.front.service.CategoryService;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
	public List<CategoryWithBLOBs> navigate(String code) {
		CategoryWithBLOBs category = null;
		if("top".equals(code)) {
			category = new CategoryWithBLOBs();
			category.setId("-1");
		}else {
			category = categoryMapper.queryCategoryByCode(code);
		}
		if(category != null) {
			List<CategoryWithBLOBs> list = getTreeList(category.getId());
			return list;
		}
		return null;
	}

	private List<CategoryWithBLOBs> getTreeList(String parentId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parentId", parentId);
		params.put("isShow", "1");
		List<CategoryWithBLOBs> list = categoryMapper.queryListByParams(params);
		if(list != null && list.size()>0) {
			for(int i= 0;i < list.size();i++) {
				CategoryWithBLOBs category = list.get(i);
				category.setNodes(getTreeList(category.getId()));
			}
		}
		return list;
	}
}
