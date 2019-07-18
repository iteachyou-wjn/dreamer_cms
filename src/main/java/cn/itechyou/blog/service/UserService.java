package cn.itechyou.blog.service;

import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.common.ResponseResult;
import cn.itechyou.blog.common.SearchEntity;
import cn.itechyou.blog.entity.User;

public interface UserService {

	User getByUserName(String username);

	ResponseResult save(User user);

	PageInfo<User> listByPage(SearchEntity page);

	ResponseResult remove(String id);

	ResponseResult getByID(String id);

}
