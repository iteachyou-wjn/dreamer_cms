package cc.iteachyou.cms.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.entity.Permission;

public interface PermissionService {

	PageInfo<Permission> queryListByPage(SearchEntity params);

	int add(Permission permission);

	Permission queryMenuById(String id);

	int update(Permission permission);

	int delete(String id);

	List<Permission> queryAll();

}
