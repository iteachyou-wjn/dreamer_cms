package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.itechyou.cms.entity.Variable;

public interface VariableMapper {
    int deleteByPrimaryKey(String id);

    int insert(Variable record);

    int insertSelective(Variable record);

    Variable selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Variable record);

    int updateByPrimaryKey(Variable record);

	List<Variable> queryListByPage(Map<String, Object> entity);

	Variable queryVariableByName(@Param("name") String name);
}