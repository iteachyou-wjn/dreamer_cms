package cn.itechyou.blog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.itechyou.blog.common.ResponseResult;
import cn.itechyou.blog.common.SearchEntity;
import cn.itechyou.blog.common.StateCodeEnum;
import cn.itechyou.blog.dao.UserMapper;
import cn.itechyou.blog.entity.User;
import cn.itechyou.blog.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public User getByUserName(String username) {
		User user = userMapper.getByUserName(username);
		return user;
	}

	@Override
	public ResponseResult save(User user) {
		try {
			userMapper.updateByPrimaryKeySelective(user);
			return ResponseResult.Factory.newInstance(Boolean.TRUE, StateCodeEnum.HTTP_SUCCESS.getCode(), null, StateCodeEnum.HTTP_SUCCESS.getDescription());
		} catch (Exception e) {
			return ResponseResult.Factory.newInstance(Boolean.FALSE, StateCodeEnum.HTTP_ERROR.getCode(), null, StateCodeEnum.HTTP_ERROR.getDescription());
		}
	}

	@Override
	public PageInfo<User> listByPage(SearchEntity page) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		List<User> list = userMapper.listByPage(page.getEntity());
		PageInfo<User> pageList = new PageInfo<>(list);
		return pageList;
	}

	@Override
	public ResponseResult remove(String id) {
		try {
			return ResponseResult.Factory.newInstance(Boolean.TRUE, StateCodeEnum.HTTP_SUCCESS.getCode(), null, StateCodeEnum.HTTP_SUCCESS.getDescription());
		} catch (Exception e) {
			return ResponseResult.Factory.newInstance(Boolean.FALSE, StateCodeEnum.HTTP_ERROR.getCode(), null, StateCodeEnum.HTTP_ERROR.getDescription());
		}
	}

	@Override
	public ResponseResult getByID(String id) {
		try {
			return ResponseResult.Factory.newInstance(Boolean.TRUE, StateCodeEnum.HTTP_SUCCESS.getCode(), null, StateCodeEnum.HTTP_SUCCESS.getDescription());
		} catch (Exception e) {
			return ResponseResult.Factory.newInstance(Boolean.FALSE, StateCodeEnum.HTTP_ERROR.getCode(), null, StateCodeEnum.HTTP_ERROR.getDescription());
		}
	}

}
