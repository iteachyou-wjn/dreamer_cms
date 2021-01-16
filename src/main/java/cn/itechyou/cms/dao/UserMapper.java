package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import cn.itechyou.cms.common.BaseMapper;
import cn.itechyou.cms.entity.User;

public interface UserMapper extends BaseMapper<User> {

    List<User> listByPage(Map<String, Object> entity);

	User getByUserName(String username);
}