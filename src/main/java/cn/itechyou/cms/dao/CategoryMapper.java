package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.itechyou.cms.common.BaseMapper;
import cn.itechyou.cms.entity.Category;

public interface CategoryMapper extends BaseMapper<Category> {

    List<Category> queryListByPage(Map<String,Object> params);

	List<Category> selectByParentId(@Param("parentId")String parentId);

	Category queryCategoryByCode(@Param("code") String code);

	List<Category> queryListByParams(Map<String, Object> params);

	List<Category> queryAll();
}