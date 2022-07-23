package cc.iteachyou.cms.dao;

import java.util.List;
import java.util.Map;

import cc.iteachyou.cms.common.BaseMapper;
import cc.iteachyou.cms.entity.Permission;

/**
 * PermissionMapper继承基类
 */
public interface PermissionMapper extends BaseMapper<Permission> {

	List<Permission> queryListByPage(Map<String, Object> entity);
}