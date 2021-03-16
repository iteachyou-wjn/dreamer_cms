package cn.itechyou.cms.service;

import java.util.List;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.ResponseResult;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.User;
import cn.itechyou.cms.entity.UserRole;

public interface UserService {

	User getByUserName(String username);

	ResponseResult save(User user);

	PageInfo<User> listByPage(SearchEntity page);

	User getByID(String id);

	int addUser(User user);

	int updateUser(User user);

	int deleteUser(String id);

	List<UserRole> queryRolesByUserId(String userId);

	int grant(String userId, List<String> roles);
}
