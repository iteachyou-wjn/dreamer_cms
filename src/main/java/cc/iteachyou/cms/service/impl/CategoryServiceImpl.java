package cc.iteachyou.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.hutool.core.map.MapUtil;
import cc.iteachyou.cms.common.ExceptionEnum;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.dao.ArchivesMapper;
import cc.iteachyou.cms.dao.CategoryMapper;
import cc.iteachyou.cms.entity.Category;
import cc.iteachyou.cms.exception.AdminGeneralException;
import cc.iteachyou.cms.exception.CmsException;
import cc.iteachyou.cms.service.CategoryService;
/**
 *      栏目管理业务类
 * @author LIGW
 *
 */
@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private ArchivesMapper archivesMapper;
	
	/**
	 * 添加
	 */
	@Override
	public void save(Category category) {
		try {
			categoryMapper.insertSelective(category);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  列表
	 */
	@Override
	public PageInfo<Category> queryListByPage(SearchEntity params) {
		if(params.getPageNum() == null || params.getPageNum() == 0) {
			params.setPageNum(1);
		}
		if(params.getPageSize() == null || params.getPageSize() == 0) {
			params.setPageSize(10);
		}
		PageHelper.startPage(params.getPageNum(), params.getPageSize());
		List<Category> list = categoryMapper.queryListByPage(params.getEntity());
		PageInfo<Category> page = new PageInfo<Category>(list);
		return page;
	}

	/**
	 *  编辑回显
	 */
	@Override
	public Category selectById(String id) {
		return categoryMapper.selectByPrimaryKey(id);
	}

	/**
	 * 查看子栏目
	 */
	@Override
	public List<Category> selectByParentId(String id) {
		return categoryMapper.selectByParentId(id) ;
	}

	/** 
	 * 修改
	 */
	@Override
	public int update(Category category) {
		return categoryMapper.updateByPrimaryKeySelective(category);
	}

	/**
	 * 删除
	 * @throws CmsException 
	 */
	@Override
	@Transactional
	public int delete(String id) throws CmsException {
		List<Category> list = categoryMapper.selectByParentId(id);
		if(list != null && list.size() > 0) {
			throw new AdminGeneralException(
					ExceptionEnum.CATEGORY_REMOVE_EXCEPTION.getCode(), 
					ExceptionEnum.CATEGORY_REMOVE_EXCEPTION.getMessage(), 
					"当前栏目存在下级栏目，删除失败");
		}
		Category category = categoryMapper.selectByPrimaryKey(id);
		return cascadeDelete(category);
	}

	@Override
	public Category queryCategoryByCode(String code) {
		return this.categoryMapper.queryCategoryByCode(code);
	}

	@Override
	public void updateSort(List<Category> list) {
		for(int i = 0;i < list.size();i++) {
			Category category = new Category();
			category.setId(list.get(i).getId());
			category.setSort(list.get(i).getSort());
			this.categoryMapper.updateByPrimaryKeySelective(category);
		}
	}
	
	@Override
	public List<Category> getTreeList(Map<String,Object> params) {
		List<Category> list = categoryMapper.queryListByParams(params);
		if(list != null && list.size()>0) {
			for(int i= 0;i < list.size();i++) {
				Category category = list.get(i);
				Map<String,Object> newEntity = MapUtil.builder(params).build();
				newEntity.put("parentId", category.getId());
				category.setNodes(getTreeList(newEntity));
			}
		}
		return list;
	}

	@Override
	public List<Category> queryListByCode(Map<String, Object> entity) {
		return categoryMapper.queryListByParams(entity);
	}

	@Override
	public List<Category> queryAll() {
		return categoryMapper.queryAll();
	}
	
	/**
	 * 级联删除栏目
	 * @param category
	 * @return
	 */
	private int cascadeDelete(Category category) {
		int counter = 0;
		if(category == null) {
			return counter;
		}
		List<Category> categorys = categoryMapper.selectByParentId(category.getId());
		for(Category temp : categorys) {
			counter += cascadeDelete(temp);
		}
		counter += categoryMapper.deleteByPrimaryKey(category.getId());
		return counter;
	}
	
}
