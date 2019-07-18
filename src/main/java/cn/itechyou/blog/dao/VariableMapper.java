package cn.itechyou.blog.dao;

import java.util.List;
import java.util.Map;

import cn.itechyou.blog.entity.Variable;

public interface VariableMapper {
    int deleteByPrimaryKey(String id);

    int insert(Variable record);

    int insertSelective(Variable record);

    Variable selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Variable record);

    int updateByPrimaryKey(Variable record);

	List<Variable> queryListByPage(Map<String, Object> entity);
}