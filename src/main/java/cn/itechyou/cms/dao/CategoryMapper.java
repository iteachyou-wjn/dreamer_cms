package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.itechyou.cms.entity.Category;
import cn.itechyou.cms.entity.CategoryWithBLOBs;

public interface CategoryMapper {
    int deleteByPrimaryKey(String id);

    int insert(CategoryWithBLOBs record);

    int insertSelective(CategoryWithBLOBs record);

    CategoryWithBLOBs selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CategoryWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(CategoryWithBLOBs record);

    int updateByPrimaryKey(Category record);

    List<CategoryWithBLOBs> queryListByPage(Map<String,Object> params);

	List<CategoryWithBLOBs> selectByParentId(@Param("parentId")String parentId);

	CategoryWithBLOBs queryCategoryByCode(@Param("code") String code);

	List<CategoryWithBLOBs> queryListByParams(Map<String, Object> params);

	List<CategoryWithBLOBs> queryAll();
}