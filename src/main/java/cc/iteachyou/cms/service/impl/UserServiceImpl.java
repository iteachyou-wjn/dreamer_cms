package cc.iteachyou.cms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.ResponseResult;
import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.common.StateCodeEnum;
import cc.iteachyou.cms.dao.UserMapper;
import cc.iteachyou.cms.dao.UserRoleMapper;
import cc.iteachyou.cms.entity.User;
import cc.iteachyou.cms.entity.UserRole;
import cc.iteachyou.cms.service.UserService;
import cc.iteachyou.cms.utils.UUIDUtils;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;

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

	@Override
	public List<UserRole> queryRolesByUserId(String userId) {
		UserRole example = new UserRole();
		example.setUserId(userId);
		return userRoleMapper.select(example);
	}

	@Override
	@Transactional
	public int grant(String userId, List<String> roles) {
		UserRole example = new UserRole();
		example.setUserId(userId);
		userRoleMapper.delete(example);
		
		List<UserRole> userRoles = new ArrayList<UserRole>();
		for(int i = 0;i < roles.size();i++) {
			UserRole ur = new UserRole();
			ur.setId(UUIDUtils.getPrimaryKey());
			ur.setUserId(userId);
			ur.setRoleId(roles.get(i));
			userRoles.add(ur);
		}
		int i = 0;
		if(userRoles.size() > 0) {
			i = userRoleMapper.insertBatchList(userRoles);
		}
		return i;
	}
}
