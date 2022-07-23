package cc.iteachyou.cms.dao;

import java.util.List;
import java.util.Map;

import cc.iteachyou.cms.common.BaseMapper;
import cc.iteachyou.cms.entity.User;

public interface UserMapper extends BaseMapper<User> {

    List<User> listByPage(Map<String, Object> entity);

	User getByUserName(String username);
}