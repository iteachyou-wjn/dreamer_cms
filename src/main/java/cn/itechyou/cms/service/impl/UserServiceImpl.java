package cn.itechyou.cms.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.itechyou.cms.common.ResponseResult;
import cn.itechyou.cms.common.SearchEntity;
import cn.itechyou.cms.common.StateCodeEnum;
import cn.itechyou.cms.dao.UserMapper;
import cn.itechyou.cms.entity.User;
import cn.itechyou.cms.service.UserService;

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
	public PageInfo<User> listByPage(SearchEntity params) {
		if(params.getPageNum() == null || params.getPageNum() == 0) {
			params.setPageNum(1);
		}
		if(params.getPageSize() == null || params.getPageSize() == 0) {
			params.setPageSize(10);
		}
		PageHelper.startPage(params.getPageNum(), params.getPageSize());
		List<User> list = userMapper.listByPage(params.getEntity());
		PageInfo<User> pageList = new PageInfo<>(list);
		return pageList;
	}

	@Override
	public User getByID(String id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public int addUser(User user) {
		return userMapper.insertSelective(user);
	}

	@Override
	public int updateUser(User user) {
		return userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public int deleteUser(String id) {
		return userMapper.deleteByPrimaryKey(id);
	}
}
