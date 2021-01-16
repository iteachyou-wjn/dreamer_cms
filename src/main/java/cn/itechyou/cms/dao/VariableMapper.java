package cn.itechyou.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.itechyou.cms.common.BaseMapper;
import cn.itechyou.cms.entity.Variable;

public interface VariableMapper extends BaseMapper<Variable> {

	List<Variable> queryListByPage(Map<String, Object> entity);

	Variable queryVariableByName(@Param("name") String name);
}