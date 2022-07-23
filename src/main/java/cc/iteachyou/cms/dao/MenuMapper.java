package cc.iteachyou.cms.dao;

import java.util.List;
import java.util.Map;

import cc.iteachyou.cms.common.BaseMapper;
import cc.iteachyou.cms.entity.Menu;

/**
 * MenuMapper继承基类
 */
public interface MenuMapper extends BaseMapper<Menu> {

	List<Menu> queryListByPage(Map<String, Object> entity);

	List<Menu> selectListByUserId(String userId, String parentId);

	List<Menu> selectListByParentId(String string);

}