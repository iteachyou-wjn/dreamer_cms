package cn.itechyou.cms.service;

import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.ResponseResult;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.entity.User;

public interface UserService {

	User getByUserName(String username);

	ResponseResult save(User user);

	PageInfo<User> listByPage(SearchEntity page);

	User getByID(String id);

}
