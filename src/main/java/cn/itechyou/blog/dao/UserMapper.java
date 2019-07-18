package cn.itechyou.blog.dao;

import java.util.List;
import java.util.Map;

import cn.itechyou.blog.entity.User;

public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> listByPage(Map<String, Object> entity);

	User getByUserName(String username);
}